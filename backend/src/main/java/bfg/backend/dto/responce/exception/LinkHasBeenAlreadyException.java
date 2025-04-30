package bfg.backend.dto.responce.exception;

import org.springframework.http.HttpStatus;

public class LinkHasBeenAlreadyException extends AbstractException{
    public LinkHasBeenAlreadyException(){
        super(HttpStatus.CONFLICT, "Такая связь уже есть");
    }
}
