package infrastructure.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

public class ExternalServiceExceptionMapper implements ResponseExceptionMapper<ExternalServiceException> {

    @Inject
    ObjectMapper objectMapper;

    @Override
    public ExternalServiceException toThrowable(Response response) {
        final int statusCode = response.getStatus();
        final String responseBody = response.readEntity(String.class);
        final String errorMessage = extractErrorMessage(responseBody);

        if (statusCode < 200 || statusCode >= 300) {
            return new ExternalServiceException(statusCode, errorMessage);
        }
        return null;
    }

    private String extractErrorMessage(String json) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            return rootNode.has("message") ? rootNode.get("message").asText() : json;
        } catch (Exception e) {
            return json;
        }
    }
}
