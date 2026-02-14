package NewsPortal.com.example.NewsPortalApp.exception;

import org.springframework.http.HttpStatus;

public class CustomApiException extends RuntimeException {

    private final HttpStatus status;

    public CustomApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

