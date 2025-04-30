package bfg.backend.controller;

import bfg.backend.dto.responce.day.ChangeDay;
import bfg.backend.service.DayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления игровыми днями.
 * Предоставляет API для работы с игровым временем и переходами между днями.
 */
@RestController
@RequestMapping(path = "day")
public class DayController {

    private final DayService dayService;

    public DayController(DayService dayService) {
        this.dayService = dayService;
    }

    /**
     * Переход к следующему игровому дню.
     * Обновляет игровое состояние, связанное с изменением дня.
     *
     * @return DTO с информацией об изменении количества ресурсов за день
     * @see ChangeDay
     */
    @GetMapping()
    public ChangeDay addDay() {
        return dayService.addDay();
    }
}