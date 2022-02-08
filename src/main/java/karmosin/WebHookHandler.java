package karmosin;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;

public interface WebHookHandler {
    /**
     * parseEvent responsible for parsing an event from the Webhook and create an
     * ContinuousIntegrationJob object.
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ContinuousIntegrationJob parseEventhandle(String target, Request baseRequest, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException;

    /**
     * responseEvent() response to an WebHook event (e.g. tell the status)
     * 
     * @throws Exception
     */
    public void responseEvent(String target, Request baseRequest, HttpServletRequest request,
            HttpServletResponse response, ContinuousIntegrationJob continuousIntegrationJob)
            throws IOException, ServletException;
}
