package crimson;
import io.github.cdimascio.dotenv.Dotenv;
import kotlin.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.HttpClient;
import java.io.IOException;
import java.util.*;

public class CommitStatusHandler {
    static Dotenv dotenv = Dotenv.load();

    public static final String ERROR = "error";
    public static final String FAILURE = "failure";
    public static final String PENDING = "pending";
    public static final String SUCCESS = "success";

    private static final String USER_NAME = "641bill";
    static final String GITHUB_TOKEN = dotenv.get("GITHUB_TOKEN");
    private static final String API_URL = "https://api.github.com/repos/kth-dd2480-vt22/assignment2/statuses/";

    private static final String context = "Group13-CI";

    /**
     * Set the commit status on GitHub to be the given state along with other arguments,
     * using GitHub REST API. It is done by sending a HTTP request to the GitHub API url.
     * @param state
     * @param target_url
     * @param description
     * @param commitHash
     * @return the HttpEntity of the response
     * @throws IOException
     */
    public HttpEntity setStatus(String state, String target_url, String description, String commitHash) throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(API_URL + commitHash);
        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        String authentication = USER_NAME + ":" + GITHUB_TOKEN;
        post.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(authentication.getBytes()));

        List<Pair<String, String>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("state", state));
        pairs.add(new Pair<>("target_url", target_url));
        pairs.add(new Pair<>("description", description));
        pairs.add(new Pair<>("context", context));
        StringJoiner joiner = new StringJoiner(", ");
        for (Pair<String, String> pair : pairs) {
            joiner.add("\"" + pair.getFirst() + "\": \"" + pair.getSecond() + "\"");
        }
        post.setEntity(new StringEntity("{" + joiner + "}"));

        HttpResponse response = client.execute(post);
        return response.getEntity();
    }

}
