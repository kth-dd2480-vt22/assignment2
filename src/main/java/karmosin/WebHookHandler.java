package karmosin;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import jakarta.servlet.ServletException;

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
    public ContinuousIntegrationJob parseEvent(String target, Request baseRequest, HttpServletRequest request,
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
