package crimson;

import org.apache.http.HttpEntity;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class CommitStatusHandlerTests {
    @Test
    public void test() {
        CommitStatusHandler handler = new CommitStatusHandler();
        String target_url = "https://www.github.com";
        String description = "test";
        String commitHash = "e29eb6b7e65f7ad008e329ce517f0f36123510f2";
        try {
            HttpEntity entity = handler.setStatus(CommitStatusHandler.SUCCESS, target_url, description, commitHash);
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            String response = reader.lines().collect(Collectors.joining("\n"));
            JSONObject json = new JSONObject(response);
            if (json.has("state")) {
                Assertions.assertEquals("success", json.getString("state"));
            } else {
                System.out.println(json);
                System.out.println(CommitStatusHandler.GITHUB_TOKEN);
                Assertions.fail("No state field in response");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
