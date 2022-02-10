import crimson.WebHookHandler;
import karmosin.ContinuousIntegrationJob;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.ServletException;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class WebHookHandlerTests {
    /**
     * Tests the functionality of the parseEvent function when parsing push events,
     * by feeding it a MockHttpServletRequest with a valid JSON payload.
     */
    @Test
    public void validTestPushParsing() throws ServletException, IOException {
        WebHookHandler handler = new WebHookHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        JSONObject obj = new JSONObject();
        JSONObject repo = new JSONObject();
        JSONObject commit = new JSONObject();
        JSONArray commits = new JSONArray();
        JSONObject author = new JSONObject();
        author.put("name", "John Doe");
        author.put("email", "123@kth.se");
        commit.put("id", "12345");
        commit.put("author", author);
        commits.put(commit);
        repo.put("url", "https://github.com/Codertocat/Hello-World");
        obj.put("repository", repo);
        obj.put("ref", "test_ref");
        obj.put("after", "test_after");
        obj.put("compare", "test_compare");
        obj.put("commits", commits);
        byte[] jsonBytes = obj.toString().getBytes();
        request.setContent(jsonBytes);
        request.setMethod("POST");
        ContinuousIntegrationJob job = handler.parseEvent("", null, request, null);

        assertEquals(job.repoGitUrl.toString(), "https://github.com/Codertocat/Hello-World");
        assertEquals(job.gitRefs, "test_ref");
        assertEquals(job.commitHash, "12345");
        assertEquals(job.pusherName, "John Doe");
        assertEquals(job.pusherEmail, "123@kth.se");
    }
}
