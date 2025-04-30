package bfg.backend.dto.responce.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Глобальный обработчик исключений для REST API.
 * Перехватывает исключения и преобразует их в стандартизированные HTTP-ответы.
 */
@ControllerAdvice
@Component
public class ExceptionResolver {

    /**
     * Обрабатывает все исключения, унаследованные от AbstractException.
     *
     * @param cause перехваченное исключение
     * @param request текущий веб-запрос
     * @return ResponseEntity с соответствующим HTTP статусом и сообщением об ошибке
     */
    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<?> handle(AbstractException cause, WebRequest request) {
        return cause.asResponse();
    }
}