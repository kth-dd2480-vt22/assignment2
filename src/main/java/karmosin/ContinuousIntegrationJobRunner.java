package karmosin;

/**
 * ContinuousIntegrationJobRunner responsible for running tasks (build, test)
 * for a project.
 */
public interface ContinuousIntegrationJobRunner {

        public ContinuousIntegrationJobTaskOutput cloneRepo(ContinuousIntegrationJob continuousIntegrationJob,
                        String targetLocation) throws Exception;

        public ContinuousIntegrationJobTaskOutput runCheck(ContinuousIntegrationJob continuousIntegrationJob,
                        String targetLocation) throws Exception;

        public ContinuousIntegrationJobTaskOutput runBuild(ContinuousIntegrationJob continuousIntegrationJob,
                        String targetLocation) throws Exception;

        public ContinuousIntegrationJobTaskOutput runTest(ContinuousIntegrationJob continuousIntegrationJob,
                        String targetLocation) throws Exception;
}
