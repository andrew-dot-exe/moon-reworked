package bfg.backend.dto.responce.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение, возникающее при попытке регистрации пользователя по email,
 * которая уже используется другим пользователем.
 * Возвращает HTTP статус 409 Conflict.
 */
public class EmailHasBeenUsedAlreadyException extends AbstractException{
    public EmailHasBeenUsedAlreadyException(){
        super(HttpStatus.CONFLICT, "Такая почта уже используется");
    }
}
