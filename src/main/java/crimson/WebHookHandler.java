package crimson;
import karmosin.*;
import org.eclipse.jetty.server.Request;
import org.json.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.stream.Collectors;

public class WebHookHandler implements karmosin.WebHookHandler {

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
            if (json.getString("after") == "0000000000000000000000000000000000000000"){
                return null;
            }
            String gitRef = json.getString("ref");
            String url = json.getJSONObject("repository").getString("url");
            URL repoUrl = new URL(url);
            JSONArray commits = json.getJSONArray("commits");
            JSONObject commit = commits.getJSONObject(0);
            String commitHash = commit.getString("id");
            String pusherName = commit.getJSONObject("author").getString("name");
            String pusherEmail = commit.getJSONObject("author").getString("email");
            job.gitRefs = gitRef;
            job.repoGitUrl = repoUrl;
            job.commitHash = commitHash;
            job.pusherName = pusherName;
            job.pusherEmail = pusherEmail;
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

    }
}
