package bfg.backend.dto.responce.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение, возникающее при попытке создать связь между областями,
 * когда такая связь уже существует.
 * Возвращает HTTP статус 409 Conflict.
 */
public class LinkHasBeenAlreadyException extends AbstractException{
    public LinkHasBeenAlreadyException(){
        super(HttpStatus.CONFLICT, "Такая связь уже есть");
    }
}
