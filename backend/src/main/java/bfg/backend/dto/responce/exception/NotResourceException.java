package bfg.backend.dto.responce.exception;

import org.springframework.http.HttpStatus;

public class NotResourceException extends AbstractException{
    public NotResourceException(){
        super(HttpStatus.NOT_FOUND, "Такого ресурса нет (как так?)");
    }
}
