package crimson;

import karmosin.ContinuousIntegrationJob;

import java.io.IOException;

public class ResultEmailer{

    public String pusherEmail;
    public boolean succeeded;
    public String message;

    public void emailResult(ContinuousIntegrationJob continuousIntegrationJob) {

        this.pusherEmail = continuousIntegrationJob.pusherEmail;
        this.succeeded = continuousIntegrationJob.succeeded;

        sendSimpleMessage();

    }

    public void sendSimpleMessage() {
        if(this.succeeded){
            message = "build launched successfully";
        }else{
            message = "build failed successfully";
        }
        ProcessBuilder b = new ProcessBuilder();
        b.command("bash", "-c", "mail -s \"Your latest push\" "+ pusherEmail +" <<< '"+ message + "'");
        try {
            b.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
