package crimson;
import karmosin.*;

import org.apache.http.HttpEntity;

import org.eclipse.jetty.server.Request;
import org.json.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

public class WebHookHandler implements karmosin.WebHookHandler {

    CommitStatusHandler commitStatusHandler;

    public WebHookHandler(String githubUsername, String repoStatusUrl, String githubToken){
        commitStatusHandler = new CommitStatusHandler(githubUsername, githubToken, repoStatusUrl, "Group13-CI");
    }

    /**
     * parseEvent responsible for parsing an event from the Webhook and create an
     * ContinuousIntegrationJob object.
     *
     * @param target
     * @param baseRequest
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    public ContinuousIntegrationJob parseEvent(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ContinuousIntegrationJob job = new ContinuousIntegrationJob();
        if (request.getMethod().equals("POST")) {
            JSONObject json = new JSONObject(request.getReader().lines().collect(Collectors.joining()));
            if (json.getString("after").equals("0000000000000000000000000000000000000000")){
                return null;
            }
            String gitRef = json.getString("ref");
            String url = json.getJSONObject("repository").getString("url");
            URL repoUrl = new URL(url);
            JSONArray commits = json.getJSONArray("commits");
            JSONObject commit = commits.getJSONObject(0);
            String commitHash = json.getString("after");
            String pusherName = commit.getJSONObject("author").getString("name");
            String pusherEmail = commit.getJSONObject("author").getString("email");
            job.gitRefs = gitRef;
            job.repoGitUrl = repoUrl;
            job.commitHash = commitHash;
            job.pusherName = pusherName;
            job.pusherEmail = pusherEmail;
            job.jobID = request.getHeader("X-GitHub-Delivery");
        }
        return job;
    }

    /**
     * responseEvent() response to an WebHook event (e.g. tell the status)
     *
     * @param target
     * @param baseRequest
     * @param request
     * @param response
     * @param continuousIntegrationJob
     * @throws Exception
     */
    @Override
    public void responseEvent(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response, ContinuousIntegrationJob continuousIntegrationJob) throws IOException, ServletException {
        String state = continuousIntegrationJob.succeeded ? "success" : "failure";
        String description = continuousIntegrationJob.succeeded ? "Build succeeded" : "Build failed";
        String targetUrl = "https://dd2480-kth.fyr.fyi/ci/" + continuousIntegrationJob.jobID + "/";
        String commitHash = continuousIntegrationJob.commitHash;
        HttpEntity resp = commitStatusHandler.setStatus(state, targetUrl, description, commitHash);
        System.out.println(new BufferedReader(new InputStreamReader(resp.getContent())).lines().collect(Collectors.joining("\n")));
    }
}
