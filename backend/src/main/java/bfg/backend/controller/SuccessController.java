package bfg.backend.controller;

import bfg.backend.dto.responce.successful.Successful;
import bfg.backend.service.SuccessfulService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для получения метрик успешности колонии.
 * Предоставляет данные о текущем уровне развития и эффективности колонии.
 */
@RestController
@RequestMapping(path = "success")
public class SuccessController {

    private final SuccessfulService successfulService;

    public SuccessController(SuccessfulService successfulService) {
        this.successfulService = successfulService;
    }

    /**
     * Возвращает комплексную оценку успешности колонии.
     * Включает числовые показатели по ключевым критериям оценки.
     *
     * @return объект с метриками успешности и итоговой оценкой
     * @see Successful
     */
    @GetMapping()
    public Successful successful() {
        return successfulService.getSuccessful();
    }
}