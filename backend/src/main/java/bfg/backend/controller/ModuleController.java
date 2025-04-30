package bfg.backend.controller;

import bfg.backend.dto.request.module.CreatedModule;
import bfg.backend.dto.responce.optimality.Optimality;
import bfg.backend.repository.module.Module;
import bfg.backend.service.ModuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для работы с модулями колонии.
 * Предоставляет API для управления модулями и анализа их оптимальности.
 */
@RestController
@RequestMapping(path = "module")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    /**
     * Возвращает данные об оптимальности размещения всех модулей.
     *
     * @return Список объектов Optimality с показателями оптимальности для каждого модуля
     * @see Optimality
     */
    @GetMapping()
    public List<Optimality> optimality() {
        return moduleService.getOptimality();
    }

    /**
     * Создает новый модуль.
     *
     * @param module DTO с данными для создания модуля
     * @return DTO с информацией об идентификаторе созданного модуля и количество материалов на его строительство
     * @see Module
     * @see CreatedModule
     */
    @PostMapping
    public CreatedModule create(@RequestBody Module module) {
        return moduleService.create(module);
    }
}