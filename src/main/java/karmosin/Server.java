package karmosin;

public interface Server {

    /**
     * Start starts a server to accept WebHook event from Github and run CI tasks
     * for each event
     * 
     * @param port
     */
    public void start(int port);
}
