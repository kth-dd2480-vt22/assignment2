package karmosin;

public interface ResultEmailer {

    /**
     * emailResult responsible for sending the result of the CI job to the pusher of
     * the event.
     * 
     * @param continuousIntegrationJob
     */
    public void emailResult(ContinuousIntegrationJob continuousIntegrationJob);
}
