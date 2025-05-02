package bfg.backend.controller;

import bfg.backend.dto.request.modulePlace.ModulePlace;
import bfg.backend.dto.responce.checkPlace.CheckedPlace;
import bfg.backend.service.CheckPlaceService;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для проверки размещения модулей на площадке.
 * Предоставляет API для валидации и проверки корректности расположения модулей.
 */
@RestController
@RequestMapping(path = "check")
public class CheckPlaceController {

    private final CheckPlaceService checkPlaceService;

    public CheckPlaceController(CheckPlaceService checkPlaceService) {
        this.checkPlaceService = checkPlaceService;
    }

    /**
     * Выполняет проверку размещения модуля на площадке.
     *
     * @param modulePlace DTO с информацией о размещаемом модуле
     * @return Результат проверки размещения, содержащий информацию о рельефе и рациональности
     * @see ModulePlace
     * @see CheckedPlace
     */
    @PostMapping
    public CheckedPlace checkPlace(@RequestBody ModulePlace modulePlace){
        return checkPlaceService.check(modulePlace);
    }
}