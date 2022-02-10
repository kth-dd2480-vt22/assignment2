package crimson;

import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import karmosin.ContinuousIntegrationJobRunner;
import karmosin.ResultEmailer;
import karmosin.WebHookHandler;

public class Server implements karmosin.Server {
    ContinuousIntegrationJobRunner runner;
    ResultEmailer emailer;
    org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server();
    ContextHandlerCollection handler = new ContextHandlerCollection();
    String ciJobDir;

    public Server(ContinuousIntegrationJobRunner runner, ResultEmailer emailer, String ciJobDir) {
        this.runner = runner;
        this.emailer = emailer;
        this.ciJobDir = ciJobDir;
    }

    public void addWebHookHandler(String path, WebHookHandler handler) {
        ContextHandler contextHandler = new ContextHandler(path);

        contextHandler.setHandler(new ContinuousIntegrationHandler(handler, runner, emailer, ciJobDir));

        this.handler.addHandler(contextHandler);
    }

    @Override
    public void start(int port) {
        server.setHandler(handler);

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        try {
            server.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}