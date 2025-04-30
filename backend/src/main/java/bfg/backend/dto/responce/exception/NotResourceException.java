package bfg.backend.dto.responce.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение, возникающее при попытке доступа к несуществующему ресурсу.
 * Может возникнуть при обходе создания колонии.
 * Возвращает HTTP статус 404 Not Found.
 */
public class NotResourceException extends AbstractException{
    public NotResourceException(){
        super(HttpStatus.NOT_FOUND, "Такого ресурса нет (как так?)");
    }
}
