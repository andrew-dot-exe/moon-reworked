package bfg.backend.dto.responce.exception;

import org.springframework.http.HttpStatus;

public class EmailHasBeenUsedAlreadyException extends AbstractException{
    public EmailHasBeenUsedAlreadyException(){
        super(HttpStatus.CONFLICT, "Такая почта уже используется");
    }
}
