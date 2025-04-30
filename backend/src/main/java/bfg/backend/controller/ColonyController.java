package bfg.backend.controller;

import bfg.backend.dto.responce.allUserInfo.AllUserInfo;
import bfg.backend.service.ColonyService;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления колонией.
 * Предоставляет API для создания и удаления колонии.
 */
@RestController
@RequestMapping(path = "colony")
public class ColonyController {

    private final ColonyService colonyService;

    public ColonyController(ColonyService colonyService) {
        this.colonyService = colonyService;
    }

    /**
     * Удаляет текущую колонию.
     * Выполняет полное удаление всех данных, связанных с колонией.
     */
    @DeleteMapping()
    public void delete() {
        colonyService.delete();
    }

    /**
     * Создает новую колонию.
     * Инициализирует все необходимые структуры данных для новой колонии.
     */
    @GetMapping
    public void create() {
        colonyService.create();
    }
}