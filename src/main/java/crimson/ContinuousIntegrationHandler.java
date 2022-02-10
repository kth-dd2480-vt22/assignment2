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

        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        if (job == null){
            return;
        }

        Runnable r = new Runnable() {
            public void run() {
                String jobName = job.jobID;

                try {
                    String tmpdir = Files.createTempDirectory("ci_job").toFile().getAbsolutePath();

                    ContinuousIntegrationJobTaskOutput output;

                    output = runner.cloneRepo(job, tmpdir);
                    System.out.println("clone: " + output.exitCode);
                    System.out.println(output.ErrorOutput);
                    System.out.println(output.StandardOutput.toString());
                    writeOutput(jobName, "clone", output);
                    if (output.exitCode != 0) {
                        emailer.emailResult(job);
                        return;
                    }

                    output = runner.runCheck(job, tmpdir);
                    System.out.println("check: " + output.exitCode);
                    System.out.println(output.ErrorOutput);
                    System.out.println(output.StandardOutput.toString());
                    writeOutput(jobName, "check", output);
                    if (output.exitCode != 0) {
                        emailer.emailResult(job);
                        return;
                    }

                    output = runner.runTest(job, tmpdir);
                    System.out.println("test: " + output.exitCode);
                    System.out.println(output.ErrorOutput);
                    System.out.println(output.StandardOutput.toString());
                    writeOutput(jobName, "test", output);
                    if (output.exitCode != 0) {
                        emailer.emailResult(job);
                        return;
                    }

                    output = runner.runBuild(job, tmpdir);
                    System.out.println("build: " + output.exitCode);
                    System.out.println(output.ErrorOutput);
                    System.out.println(output.StandardOutput.toString());
                    writeOutput(jobName, "build", output);
                    if (output.exitCode != 0) {
                        emailer.emailResult(job);
                        return;
                    }

                    job.succeeded = true;

                    emailer.emailResult(job);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(r).start();

    }

    void writeOutput(String jobName, String taskName, ContinuousIntegrationJobTaskOutput output) {
        // TODO: for presistance build
    }

}
