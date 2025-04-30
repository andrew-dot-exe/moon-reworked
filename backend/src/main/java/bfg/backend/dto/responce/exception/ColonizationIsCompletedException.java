package bfg.backend.dto.responce.exception;

import org.springframework.http.HttpStatus;

public class ColonizationIsCompletedException extends AbstractException{
    public ColonizationIsCompletedException(){
        super(HttpStatus.CONFLICT, "Данный пользоваель завершил колнизацию");
    }
}
