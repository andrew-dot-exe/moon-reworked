package bfg.backend.dto.responce.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение, возникающее при попытке выполнить действие,
 * требующее незавершенной колонизации, когда колонизация уже завершена.
 * Возвращает HTTP статус 409 Conflict
 */
public class ColonizationIsCompletedException extends AbstractException{
    public ColonizationIsCompletedException(){
        super(HttpStatus.CONFLICT, "Данный пользователь завершил колонизацию");
    }
}
