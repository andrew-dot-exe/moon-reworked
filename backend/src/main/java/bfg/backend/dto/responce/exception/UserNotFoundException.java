package bfg.backend.dto.responce.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение, возникающее при попытке аутентификации несуществующего пользователя
 * или при вводе неверных учетных данных.
 * Также возникает, если пользователь не был найден по токену.
 * Возвращает HTTP статус 404 Not Found.
 */
public class UserNotFoundException extends AbstractException{
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Неверная почта или пароль");
    }
}
