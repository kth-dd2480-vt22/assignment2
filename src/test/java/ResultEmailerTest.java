import crimson.ResultEmailer;
import org.junit.jupiter.api.Test;
import karmosin.ContinuousIntegrationJob;
import static org.junit.jupiter.api.Assertions.*;

public class ResultEmailerTest {

    @Test
    public void testEmailer(){
        ResultEmailer re = new ResultEmailer();
        ContinuousIntegrationJob job = new ContinuousIntegrationJob();
        job.pusherEmail = "filiplarsback@gmail.com";
        job.succeeded = true;
    }

}
