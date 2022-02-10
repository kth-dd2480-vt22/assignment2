package crimson;

import karmosin.ContinuousIntegrationJob;
import karmosin.ContinuousIntegrationJobRunner;
import karmosin.ContinuousIntegrationJobTaskOutput;

import java.io.File;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

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
    public ContinuousIntegrationJobTaskOutput cloneRepo(ContinuousIntegrationJob continuousIntegrationJob, String targetLocation) {
        ContinuousIntegrationJobTaskOutput jobTaskOutput = new ContinuousIntegrationJobTaskOutput();
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream ps = new java.io.PrintStream(baos);
        java.io.PrintStream old = System.out;
        System.setOut(ps);
        try {
            Git.cloneRepository()
                .setURI(String.valueOf(continuousIntegrationJob.repoGitUrl))
                .setDirectory(new File(targetLocation))
                .setBranchesToClone(List.of(continuousIntegrationJob.gitRefs))
                .setBranch(continuousIntegrationJob.gitRefs)
                .call();
            continuousIntegrationJob.succeeded = true;
        }
        catch (GitAPIException e){
            continuousIntegrationJob.succeeded = false;
            jobTaskOutput.exitCode = -1;
            jobTaskOutput.ErrorOutput = String.valueOf(e);
        }
        jobTaskOutput.StandardOutput = baos.toString();
        System.out.flush();
        System.setOut(old);
        return jobTaskOutput;
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
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream ps = new java.io.PrintStream(baos);
        java.io.PrintStream old = System.out;
        System.setOut(ps);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command.split(" "));
        builder.directory(new File(targetLocation));
        Process process = builder.start();
        int exitCode = process.waitFor();
        jobTaskOutput.exitCode = exitCode;
        if (exitCode != 0 ) {
            jobTaskOutput.ErrorOutput = String.valueOf(process.getErrorStream());
        }
        continuousIntegrationJob.succeeded = jobTaskOutput.exitCode == 0;
        jobTaskOutput.StandardOutput = baos.toString();
        System.out.flush();
        System.setOut(old);
        return jobTaskOutput;
    }
}
