package bfg.backend.controller;

import bfg.backend.dto.request.module.CreatedModule;
import bfg.backend.dto.responce.moduleType.ModuleType;
import bfg.backend.dto.responce.optimality.Optimality;
import bfg.backend.repository.module.Module;
import bfg.backend.service.ModuleService;
import bfg.backend.service.logic.TypeModule;
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
     * @return Идентификатор созданного модуля
     * @see Module
     */
    @PostMapping
    public Long create(@RequestBody Module module) {
        return moduleService.create(module);
    }

    /**
     * Возвращает все типы модулей с их характеристиками.
     *
     * @return Список типов модулей
     * @see ModuleType
     */
    @GetMapping(path = "types")
    public List<ModuleType> getModuleTypes(){
        return TypeModule.getTypes();
    }
}