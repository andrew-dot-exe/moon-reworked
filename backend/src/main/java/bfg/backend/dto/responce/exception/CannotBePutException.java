package bfg.backend.dto.responce.exception;

import org.springframework.http.HttpStatus;

public class CannotBePutException extends AbstractException{
    public CannotBePutException(){
        super(HttpStatus.CONFLICT, "Нельзя поставить в этом месте");
    }
}
