package crimson;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import karmosin.ContinuousIntegrationJob;

public class ResultEmailer{

    private static final String YOUR_DOMAIN_NAME = "sandboxfa8ac3414aae4acd9eecd9a9bc3659fe.mailgun.org";
    private static final String API_KEY = "acddba0d86666190acbfe78bfda6dcb4-d2cc48bc-dfb53f9a";
    public String pusherEmail;
    public boolean succeeded;
    public String message;

    public void emailResult(ContinuousIntegrationJob continuousIntegrationJob) {

        this.pusherEmail = continuousIntegrationJob.pusherEmail;
        this.succeeded = continuousIntegrationJob.succeeded;

        try{
            JsonNode js = sendSimpleMessage();
            System.out.println(js.toString());
        } catch(UnirestException e){
            e.printStackTrace();
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
