package bfg.backend.dto.responce.exception;

import org.springframework.http.HttpStatus;

public class NotRefreshTokenException extends AbstractException{
    public NotRefreshTokenException(){
        super(HttpStatus.NOT_FOUND, "Данного refresh токена нет, или истекло время его действия");
    }
}
