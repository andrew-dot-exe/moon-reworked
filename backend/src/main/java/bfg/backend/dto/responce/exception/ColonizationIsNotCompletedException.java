package bfg.backend.dto.responce.exception;

import org.springframework.http.HttpStatus;

public class ColonizationIsNotCompletedException extends AbstractException{
    public ColonizationIsNotCompletedException(){
        super(HttpStatus.CONFLICT, "У пользователя уже есть колония");
    }
}
