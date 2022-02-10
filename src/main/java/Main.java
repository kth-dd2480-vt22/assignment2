import karmosin.ContinuousIntegrationJobRunner;
import karmosin.ResultEmailer;
import karmosin.WebHookHandler;

public class Main {
    public static void main(String[] args) {
        WebHookHandler githubWebHooker = new crimson.WebHookHandler(System.getenv("GITHUB_USERNAME"),
                System.getenv("GITHUB_REPOSTATUSURL"), System.getenv("GITHUB_TOKEN"));
        ContinuousIntegrationJobRunner runner = new crimson.JobRunner();

        String emailDomain = System.getenv("MAILGUN_DOMAIN");
        String mailgunKey = System.getenv("MAILGUN_KEY");

        ResultEmailer emailer = new crimson.ResultEmailer(emailDomain, mailgunKey);
        crimson.Server server = new crimson.Server(runner, emailer, ".ci");

        server.addWebHookHandler("/github", githubWebHooker);
        server.start(10080);
    }

}
