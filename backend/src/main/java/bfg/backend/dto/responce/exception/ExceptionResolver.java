package bfg.backend.dto.responce.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Component
public class ExceptionResolver {

    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<?> handle(AbstractException cause, WebRequest request){
        return cause.asResponse();
    }
}
