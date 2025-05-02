package bfg.backend.dto.responce.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение, выбрасываемое при попытке создать новую колонию,
 * когда у пользователя уже есть активная (незавершенная) колония.
 * Возвращает HTTP статус 409 Conflict.
 */
public class ColonizationIsNotCompletedException extends AbstractException{
    public ColonizationIsNotCompletedException(){
        super(HttpStatus.CONFLICT, "У пользователя уже есть колония");
    }
}
