package crimson;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import karmosin.ContinuousIntegrationJob;
import karmosin.ContinuousIntegrationJobRunner;
import karmosin.ContinuousIntegrationJobTaskOutput;
import karmosin.ResultEmailer;
import karmosin.WebHookHandler;

public class ContinuousIntegrationHandler extends AbstractHandler {
    WebHookHandler handler;
    ContinuousIntegrationJobRunner runner;
    ResultEmailer emailer;
    String ciJobDir;

    public ContinuousIntegrationHandler(WebHookHandler handler, ContinuousIntegrationJobRunner runner,
            ResultEmailer emailer,
            String ciJobDir) {
        this.handler = handler;
        this.runner = runner;
        this.emailer = emailer;
        this.ciJobDir = ciJobDir;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        ContinuousIntegrationJob job = handler.parseEvent(target, baseRequest, request, response);

        String jobName = "";

        try {
            String tmpdir = Files.createTempDirectory("ci_job").toFile().getAbsolutePath();

            ContinuousIntegrationJobTaskOutput output;

            output = this.runner.cloneRepo(job, tmpdir);
            writeOutput(jobName, "clone", output);
            if (output.exitCode != 0) {
                emailer.emailResult(job);
                return;
            }

            output = this.runner.runCheck(job, tmpdir);
            writeOutput(jobName, "check", output);
            if (output.exitCode != 0) {
                emailer.emailResult(job);
                return;
            }

            output = this.runner.cloneRepo(job, tmpdir);
            writeOutput(jobName, "test", output);
            if (output.exitCode != 0) {
                emailer.emailResult(job);
                return;
            }

            output = this.runner.cloneRepo(job, tmpdir);
            writeOutput(jobName, "build", output);
            if (output.exitCode != 0) {
                emailer.emailResult(job);
                return;
            }

            job.succeeded = true;

            emailer.emailResult(job);

        } catch (IOException e) {
            throw e;
        } catch (ServletException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void writeOutput(String jobName, String taskName, ContinuousIntegrationJobTaskOutput output) {
        // TODO: for presistance build
    }

}
