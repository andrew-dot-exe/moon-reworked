package bfg.backend.dto.responce.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@JsonIgnoreProperties(value = {"cause", "stackTrace", "suppressed", "localizedMessage"})
public abstract class AbstractException extends RuntimeException{

    protected final HttpStatus httpStatus;

    protected final String message;

    public ResponseEntity<?> asResponse() {
        return ResponseEntity.status(httpStatus).body(message);
    }

    public AbstractException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public AbstractException() {
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        message = "Ошибка сервера";
    }
}
