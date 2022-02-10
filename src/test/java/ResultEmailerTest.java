import com.mashape.unirest.http.exceptions.UnirestException;
import crimson.ResultEmailer;
import org.junit.jupiter.api.Test;


public class ResultEmailerTest {

    @Test
    public void testEmailer(){

        ResultEmailer re = new ResultEmailer();
        re.pusherEmail = "filiplarsback@gmail.com";
        try{
            re.sendSimpleMessage();
            }catch(UnirestException e){
            e.printStackTrace();
        }

    }

}
