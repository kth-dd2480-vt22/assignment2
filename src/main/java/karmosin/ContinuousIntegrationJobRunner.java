package karmosin;

/**
 * ContinuousIntegrationJobRunner responsible for running tasks (build, test)
 * for a project.
 */
public interface ContinuousIntegrationJobRunner {

        /**
         * clone the repo to the targetLocation (`git clone $repoUrl $targetLocation`)
         * 
         * @param continuousIntegrationJob
         * @param targetLocation
         * @return
         * @throws Exception
         */
        public ContinuousIntegrationJobTaskOutput cloneRepo(ContinuousIntegrationJob continuousIntegrationJob,
                        String targetLocation) throws Exception;

        /**
         * run check in target location (`cd $targetLocation; gradle check`)
         * 
         * @param continuousIntegrationJob
         * @param targetLocation
         * @return
         * @throws Exception
         */
        public ContinuousIntegrationJobTaskOutput runCheck(ContinuousIntegrationJob continuousIntegrationJob,
                        String targetLocation) throws Exception;

        /**
         * run build in target location (`cd $targetLocation; gradle build`)
         * 
         * @param continuousIntegrationJob
         * @param targetLocation
         * @return
         * @throws Exception
         */
        public ContinuousIntegrationJobTaskOutput runBuild(ContinuousIntegrationJob continuousIntegrationJob,
                        String targetLocation) throws Exception;

        /**
         * run test in target location (`cd $targetLocation; gradle test`)
         * 
         * @param continuousIntegrationJob
         * @param targetLocation
         * @return
         * @throws Exception
         */
        public ContinuousIntegrationJobTaskOutput runTest(ContinuousIntegrationJob continuousIntegrationJob,
                        String targetLocation) throws Exception;
}
