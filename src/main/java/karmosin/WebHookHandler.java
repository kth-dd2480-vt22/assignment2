package karmosin;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
    public ContinuousIntegrationJob parseEvent(HttpRequest request, HttpResponse response) throws Exception;

    /**
     * responseEvent() response to an WebHook event (e.g. tell the status)
     * 
     * @throws Exception
     */
    public void responseEvent(HttpRequest request, HttpResponse response,
            ContinuousIntegrationJob continuousIntegrationJob) throws Exception;
}
