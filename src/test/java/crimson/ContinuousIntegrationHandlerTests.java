package crimson;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.junit.Test;

import karmosin.ContinuousIntegrationJob;
import karmosin.ContinuousIntegrationJobTaskOutput;

public class ContinuousIntegrationHandlerTests {

    /**
     * Mock ResultEmailer to assert the job result is as expected
     */
    class MockResultEmailer implements karmosin.ResultEmailer {
        boolean expectSucceeded;

        public MockResultEmailer(boolean expectSucceeded) {
            this.expectSucceeded = expectSucceeded;
        }

        @Override
        public void emailResult(ContinuousIntegrationJob continuousIntegrationJob) {
            assertTrue(continuousIntegrationJob.succeeded == expectSucceeded,
                    "ContinuousIntegrationJob.succeeded != expectSucceeded");
        }

    }

    /**
     * Mock Job Runner to always return tasks output with the given exitCode
     */
    class MockContinuousJobRunner implements karmosin.ContinuousIntegrationJobRunner {
        ContinuousIntegrationJobTaskOutput output;

        public MockContinuousJobRunner(int exitCode) {
            output = new ContinuousIntegrationJobTaskOutput();
            output.exitCode = exitCode;
        }

        @Override
        public ContinuousIntegrationJobTaskOutput cloneRepo(ContinuousIntegrationJob continuousIntegrationJob,
                String targetLocation) throws Exception {
            return output;
        }

        @Override
        public ContinuousIntegrationJobTaskOutput runCheck(ContinuousIntegrationJob continuousIntegrationJob,
                String targetLocation) throws Exception {
            return output;
        }

        @Override
        public ContinuousIntegrationJobTaskOutput runBuild(ContinuousIntegrationJob continuousIntegrationJob,
                String targetLocation) throws Exception {
            return output;
        }

        @Override
        public ContinuousIntegrationJobTaskOutput runTest(ContinuousIntegrationJob continuousIntegrationJob,
                String targetLocation) throws Exception {
            return output;
        }

    }

    /**
     * Mock WebHookHandler to return a dummy job
     */
    class MockWebHookHandler implements karmosin.WebHookHandler {
        public ContinuousIntegrationJob job;

        public MockWebHookHandler() {
            this.job = new ContinuousIntegrationJob();
        }

        @Override
        public ContinuousIntegrationJob parseEvent(String target, Request baseRequest, HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            return this.job;
        }

        @Override
        public void responseEvent(String target, Request baseRequest, HttpServletRequest request,
                HttpServletResponse response, ContinuousIntegrationJob continuousIntegrationJob)
                throws IOException, ServletException {
            // TODO Auto-generated method stub

        }

    }

    /**
     * try to assert ContinuousIntegrationHandler is behaiving correctly if jobs are
     * executed correctly
     * 
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testRunningCIJobSuccessfully() throws IOException, ServletException {
        MockWebHookHandler mockWebHookHandler = new MockWebHookHandler();

        ContinuousIntegrationHandler h = new ContinuousIntegrationHandler(mockWebHookHandler,
                new MockContinuousJobRunner(0),
                new MockResultEmailer(true),
                "null");

        h.handle("target", null, null, null);

        assertTrue(mockWebHookHandler.job.succeeded);
    }

    /**
     * try to asser ContinuousIntegrationHandler is behaiving correctly if any jobs
     * are failed to execute
     * 
     * @throws IOException
     * @throws ServletException
     */
    @Test
    public void testRunningCIJobFailed() throws IOException, ServletException {
        MockWebHookHandler mockWebHookHandler = new MockWebHookHandler();

        ContinuousIntegrationHandler h = new ContinuousIntegrationHandler(mockWebHookHandler,
                new MockContinuousJobRunner(1),
                new MockResultEmailer(false),
                "null");

        h.handle("target", null, null, null);

        assertFalse(mockWebHookHandler.job.succeeded);
    }
}
