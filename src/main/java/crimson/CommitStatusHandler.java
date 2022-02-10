package crimson;
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

    public static final String ERROR = "error";
    public static final String FAILURE = "failure";
    public static final String PENDING = "pending";
    public static final String SUCCESS = "success";

    private String USER_NAME;
    private String GITHUB_TOKEN;
    private String API_URL;
    private String context;

    public CommitStatusHandler() {
    }

    public CommitStatusHandler(String USER_NAME, String GITHUB_TOKEN, String API_URL, String context) {
        this.USER_NAME = USER_NAME;
        this.GITHUB_TOKEN = GITHUB_TOKEN;
        this.API_URL = API_URL;
        this.context = context;
    }

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
