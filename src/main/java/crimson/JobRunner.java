package crimson;

import karmosin.ContinuousIntegrationJob;
import karmosin.ContinuousIntegrationJobRunner;
import karmosin.ContinuousIntegrationJobTaskOutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;



/**
 * A class that handles cloning, checking, building and testing of the repo for an incoming CI-job.
 */
public class JobRunner implements ContinuousIntegrationJobRunner {

    /**
     * Clones the repo specified by the CI-job
     * @param continuousIntegrationJob the CI-job containing the information about which repo should be cloned
     * @param targetLocation the location to where the repo will be cloned
     * @return a ContinuousIntegrationJobTaskOutput object containing information about the cloning process
     */
    public ContinuousIntegrationJobTaskOutput cloneRepo(ContinuousIntegrationJob continuousIntegrationJob, String targetLocation) throws Exception {
        String cmd = "git clone " + continuousIntegrationJob.repoGitUrl + " . ";
        ContinuousIntegrationJobTaskOutput cloneOutput = runCommand(continuousIntegrationJob, targetLocation, cmd);

        cmd = "git checkout " + continuousIntegrationJob.commitHash;
        ContinuousIntegrationJobTaskOutput checkoutOutput = runCommand(continuousIntegrationJob, targetLocation, cmd);

        ContinuousIntegrationJobTaskOutput output = new ContinuousIntegrationJobTaskOutput();
        output.exitCode = cloneOutput.exitCode + checkoutOutput.exitCode;
        output.StandardOutput = cloneOutput.StandardOutput + "\n" + checkoutOutput.StandardOutput;
        output.ErrorOutput = cloneOutput.ErrorOutput + "\n" + checkoutOutput.ErrorOutput;

        return output;
    }

    /**
     * Runs the command "gradle check" on a previously cloned repo
     * @param continuousIntegrationJob the job which triggered the cloning of the repo
     * @param targetLocation the location of the cloned repo
     * @return a ContinuousIntegrationJobTaskOutput object containing information about the checking process
     */
    public ContinuousIntegrationJobTaskOutput runCheck(ContinuousIntegrationJob continuousIntegrationJob,
                                                       String targetLocation) throws Exception {
        return runCommand(continuousIntegrationJob, targetLocation, "gradle check");
    }

    /**
     * Runs the command "gradle build" on a previously cloned repo to build it
     * @param continuousIntegrationJob the job which triggered the cloning of the repo
     * @param targetLocation the location of the cloned repo
     * @return a ContinuousIntegrationJobTaskOutput object containing information about the building process
     */
    public ContinuousIntegrationJobTaskOutput runBuild(ContinuousIntegrationJob continuousIntegrationJob,
                                                       String targetLocation) throws Exception {
        return runCommand(continuousIntegrationJob, targetLocation, "gradle build");
    }

    /**
     * Runs the command "gradle test" on a previously cloned repo to run tests
     * @param continuousIntegrationJob the job which triggered the cloning of the repo
     * @param targetLocation the location of the cloned repo
     * @return a ContinuousIntegrationJobTaskOutput object containing information about the testing process
     */
    public ContinuousIntegrationJobTaskOutput runTest(ContinuousIntegrationJob continuousIntegrationJob,
                                                       String targetLocation) throws Exception {
        return runCommand(continuousIntegrationJob, targetLocation, "gradle test");
    }

    /**
     * Runs an arbitrary command on the command line and logs information about the process
     * @param continuousIntegrationJob the job which triggered the cloning of the repo
     * @param targetLocation the path where the command should be executed
     * @return a ContinuousIntegrationJobTaskOutput object containing information about the process
     */
    public ContinuousIntegrationJobTaskOutput runCommand(ContinuousIntegrationJob continuousIntegrationJob,
                                                         String targetLocation, String command) throws Exception{
        ContinuousIntegrationJobTaskOutput jobTaskOutput = new ContinuousIntegrationJobTaskOutput();
 
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);
        builder.directory(new File(targetLocation));
        Process process = builder.start();
        int exitCode = process.waitFor();
        jobTaskOutput.exitCode = exitCode;

        BufferedReader stdOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errOutput = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        jobTaskOutput.StandardOutput = stdOutput.lines().collect(Collectors.joining());
        jobTaskOutput.ErrorOutput = errOutput.lines().collect(Collectors.joining());
        
        continuousIntegrationJob.succeeded = jobTaskOutput.exitCode == 0;

        return jobTaskOutput;
    }
}
