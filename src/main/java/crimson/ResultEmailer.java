package crimson;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import karmosin.ContinuousIntegrationJob;

public class ResultEmailer implements karmosin.ResultEmailer{

    public String pusherEmail;
    public boolean succeeded;
    public String message;
    private final String YOUR_DOMAIN_NAME;
    private final String API_KEY;

    public ResultEmailer(){
        this.YOUR_DOMAIN_NAME = null;
        this.API_KEY = null;
    }

    public ResultEmailer(String YOUR_DOMAIN_NAME,  String API_KEY) {
        this.YOUR_DOMAIN_NAME = YOUR_DOMAIN_NAME;
        this.API_KEY = API_KEY;
    }

    public void emailResult(ContinuousIntegrationJob continuousIntegrationJob) {

        this.pusherEmail = continuousIntegrationJob.pusherEmail;
        this.succeeded = continuousIntegrationJob.succeeded;
        if (YOUR_DOMAIN_NAME == null || API_KEY == null) {
            return;
        } else {
            try {
                JsonNode js = sendSimpleMessage();
                System.out.println(js.toString());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return;
        }
    }

    public JsonNode sendSimpleMessage() throws UnirestException {
        if(this.succeeded){
            message = "build launched successfully";
        }else{
            message = "build failed successfully";
        }
        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + YOUR_DOMAIN_NAME + "/messages")
            .basicAuth("api", API_KEY)
            .queryString("from", "Excited User filiplarsback@gmail.com")
            .queryString("to", pusherEmail)
            .queryString("subject", "Build status")
            .queryString("text", message)
            .asJson();

        return request.getBody();
    }
}
