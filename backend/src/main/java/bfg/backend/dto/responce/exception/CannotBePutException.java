package bfg.backend.dto.responce.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение, выбрасываемое при невозможности размещения объекта
 * в запрошенном месте (непроходимый рельеф, занятая территория и т.д.)
 * Возвращает HTTP статус 409 Conflict
 */
public class CannotBePutException extends AbstractException{
    public CannotBePutException(){
        super(HttpStatus.CONFLICT, "Нельзя поставить в этом месте");
    }
}
