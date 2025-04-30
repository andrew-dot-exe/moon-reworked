package bfg.backend.dto.responce.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AbstractException{
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Неверная почта или пароль");
    }
}
