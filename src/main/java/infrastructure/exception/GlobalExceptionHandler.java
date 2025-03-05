package infrastructure.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Provider
class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof ExternalServiceException externalServiceException) {
            ErrorResponse errorResponse = new ErrorResponse(externalServiceException.getStatusCode(), externalServiceException.getMessage());
            return Response.status(errorResponse.getStatusCode()).entity(errorResponse).build();
        } else {
            ErrorResponse errorResponse = new ErrorResponse(500, "Internal Server Error");
            return Response.status(errorResponse.getStatusCode()).entity(errorResponse).build();
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class ErrorResponse {
        private int statusCode;
        private String message;
    }
}
