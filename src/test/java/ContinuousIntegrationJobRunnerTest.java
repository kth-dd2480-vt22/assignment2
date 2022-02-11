import crimson.JobRunner;
import karmosin.ContinuousIntegrationJob;

import java.net.URL;

import java.nio.file.Files;

import karmosin.ContinuousIntegrationJobTaskOutput;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ContinuousIntegrationJobRunnerTest {

    /**
     * A test case where a repo should be successfully cloned
     */
    @Test
    public void validClone() throws Exception {
        JobRunner jobRunner = new JobRunner();
        ContinuousIntegrationJob continuousIntegrationJob = new ContinuousIntegrationJob();
        String urlString = "https://github.com/tobiasgg/DD2480-A2-cloneTest.git";
        continuousIntegrationJob.repoGitUrl = new URL(urlString);
        continuousIntegrationJob.commitHash = "main";
        String targetLocation = Files.createTempDirectory("ci_job").toFile().getAbsolutePath();
        ContinuousIntegrationJobTaskOutput jobTaskOutput = jobRunner.cloneRepo(continuousIntegrationJob, targetLocation);
        assertTrue(continuousIntegrationJob.succeeded);
    }
    /**
     * A test case where a repo should be unsuccessfully cloned
     */
    @Test
    public void invalidClone() throws Exception {
        JobRunner jobRunner = new JobRunner();
        ContinuousIntegrationJob continuousIntegrationJob = new ContinuousIntegrationJob();
        String urlString = "https://github.com/<this-is-not-a-repo>";
        continuousIntegrationJob.repoGitUrl = new URL(urlString);
        continuousIntegrationJob.commitHash = "main";
        String targetLocation = Files.createTempDirectory("ci_job").toFile().getAbsolutePath();
        jobRunner.cloneRepo(continuousIntegrationJob, targetLocation);
        assertFalse(continuousIntegrationJob.succeeded);
    }

    /**
     * A test case where a repo should be successfully cloned and "gradle check" should succeed
     */
    @Test
    public void validCheck() throws Exception{
        JobRunner jobRunner = new JobRunner();
        ContinuousIntegrationJob continuousIntegrationJob = new ContinuousIntegrationJob();
        String urlString = "https://github.com/tobiasgg/DD2480-A2-buildValid.git";
        continuousIntegrationJob.repoGitUrl = new URL(urlString);
        continuousIntegrationJob.commitHash = "main";
        String targetLocation = Files.createTempDirectory("ci_job").toFile().getAbsolutePath();
        jobRunner.cloneRepo(continuousIntegrationJob, targetLocation);
        jobRunner.runCheck(continuousIntegrationJob, targetLocation);
        assertTrue(continuousIntegrationJob.succeeded);

    }

    /**
     * A test case where a repo should be successfully cloned and "gradle check" should fail
     */
    @Test
    public void invalidCheck() throws Exception{
        JobRunner jobRunner = new JobRunner();
        ContinuousIntegrationJob continuousIntegrationJob = new ContinuousIntegrationJob();
        String urlString = "https://github.com/tobiasgg/DD2480-A2-cloneTest.git";
        continuousIntegrationJob.repoGitUrl = new URL(urlString);
        continuousIntegrationJob.commitHash = "main";
        String targetLocation = Files.createTempDirectory("ci_job").toFile().getAbsolutePath();
        jobRunner.cloneRepo(continuousIntegrationJob, targetLocation);
        assertTrue(continuousIntegrationJob.succeeded);
        jobRunner.runCheck(continuousIntegrationJob, targetLocation);
        assertFalse(continuousIntegrationJob.succeeded);

    }

    /**
     * A test case where a repo should be successfully cloned and "gradle build" should succeed
     */
    @Test
    public void validBuild() throws Exception{
        JobRunner jobRunner = new JobRunner();
        ContinuousIntegrationJob continuousIntegrationJob = new ContinuousIntegrationJob();
        String urlString = "https://github.com/tobiasgg/DD2480-A2-buildValid.git";
        continuousIntegrationJob.repoGitUrl = new URL(urlString);
        continuousIntegrationJob.commitHash = "main";
        String targetLocation = Files.createTempDirectory("ci_job").toFile().getAbsolutePath();
        jobRunner.cloneRepo(continuousIntegrationJob, targetLocation);
        assertTrue(continuousIntegrationJob.succeeded);
        jobRunner.runBuild(continuousIntegrationJob, targetLocation);
        assertTrue(continuousIntegrationJob.succeeded);

    }

    /**
     * A test case where a repo should be successfully cloned and "gradle build" should fail
     */
    @Test
    public void invalidBuild() throws Exception {
        JobRunner jobRunner = new JobRunner();
        ContinuousIntegrationJob continuousIntegrationJob = new ContinuousIntegrationJob();
        String urlString = "https://github.com/tobiasgg/DD2480-A2-cloneTest.git";
        continuousIntegrationJob.repoGitUrl = new URL(urlString);
        continuousIntegrationJob.commitHash = "main";
        String targetLocation = Files.createTempDirectory("ci_job").toFile().getAbsolutePath();
        jobRunner.cloneRepo(continuousIntegrationJob, targetLocation);
        assertTrue(continuousIntegrationJob.succeeded);
        jobRunner.runBuild(continuousIntegrationJob, targetLocation);
        assertFalse(continuousIntegrationJob.succeeded);
    }

    /**
     * A test case where a repo should be successfully cloned and "gradle test" should succeed
     */
    @Test
    public void validTest() throws Exception {
        JobRunner jobRunner = new JobRunner();
        ContinuousIntegrationJob continuousIntegrationJob = new ContinuousIntegrationJob();
        String urlString = "https://github.com/tobiasgg/DD2480-testValid.git";
        continuousIntegrationJob.repoGitUrl = new URL(urlString);
        continuousIntegrationJob.commitHash = "main";
        String targetLocation = Files.createTempDirectory("ci_job").toFile().getAbsolutePath();
        jobRunner.cloneRepo(continuousIntegrationJob, targetLocation);
        assertTrue(continuousIntegrationJob.succeeded);
        jobRunner.runTest(continuousIntegrationJob, targetLocation);
        assertTrue(continuousIntegrationJob.succeeded);

    }

    /**
     * A test case where a repo should be successfully cloned and "gradle test" should fail
     */
    @Test
    public void invalidTest() throws Exception {
        JobRunner jobRunner = new JobRunner();
        ContinuousIntegrationJob continuousIntegrationJob = new ContinuousIntegrationJob();
        String urlString = "https://github.com/tobiasgg/DD2480-testInvalid.git";
        continuousIntegrationJob.repoGitUrl = new URL(urlString);
        continuousIntegrationJob.commitHash = "main";
        String targetLocation = Files.createTempDirectory("ci_job").toFile().getAbsolutePath();
        jobRunner.cloneRepo(continuousIntegrationJob, targetLocation);
        assertTrue(continuousIntegrationJob.succeeded);
        jobRunner.runTest(continuousIntegrationJob, targetLocation);
        assertFalse(continuousIntegrationJob.succeeded);
    }


}
