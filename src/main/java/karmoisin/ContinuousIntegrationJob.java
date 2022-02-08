package karmoisin;

import java.net.URL;

public class ContinuousIntegrationJob {
    /**
     * repoGitUrl is the URL of the git repo that the job belongs to.
     */
    public URL repoGitUrl;

    /**
     * gitRefs is the full git reference of the job belongs to (e.g.
     * refs/heads/main)
     */
    public String gitRefs;

    /**
     * commitHash is refering to the commit which the CI shall run for.
     */
    public String commitHash;

    /**
     * pusherName is the name of the person who pushed the commit.
     */
    public String pusherName;

    /**
     * pusherEmail is the email of the person who pushed the commit.
     */
    public String pusherEmail;

    /**
     * succeeded indicates if the job has been executed successfully or not.
     */
    public boolean succeeded;
}
