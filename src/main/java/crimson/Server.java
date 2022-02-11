package crimson;

import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.Slf4jRequestLogWriter;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;

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

        ResourceHandler rh = new ResourceHandler();
        rh.setDirAllowed(true);
        rh.setResourceBase(ciJobDir);

        ContextHandler contextHandler = new ContextHandler("/ci");
        contextHandler.setHandler(rh);

        handler.addHandler(contextHandler);
    }

    public void addWebHookHandler(String path, WebHookHandler handler) {
        ContextHandler contextHandler = new ContextHandler(path);

        contextHandler.setHandler(new ContinuousIntegrationHandler(handler, runner, emailer, ciJobDir));

        this.handler.addHandler(contextHandler);
    }

    @Override
    public void start(int port) {
        ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory());
        connector.setPort(port);
        server.addConnector(connector);
        server.setRequestLog(new CustomRequestLog(new Slf4jRequestLogWriter(), CustomRequestLog.EXTENDED_NCSA_FORMAT));

        server.setHandler(handler);

        try {
            server.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
