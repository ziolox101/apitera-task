package infrastructure.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExternalServiceException extends RuntimeException {
    private int statusCode;
    private String message;

    public ExternalServiceException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }

}
