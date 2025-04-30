package bfg.backend.controller;

import bfg.backend.dto.responce.area.CellsOfZone;
import bfg.backend.dto.responce.area.Zone;
import bfg.backend.mapping.MappingToResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для работы с зонами и ячейками площадки.
 * Предоставляет API для получения информации о зонах и их ячейках.
 */
@RestController
@RequestMapping(path = "area")
public class AreaController {

    /**
     * Получение списка всех зон площадки.
     *
     * @return Список объектов Zone, содержащих информацию о зонах
     * @see Zone
     */
    @GetMapping(path = "zones")
    public List<Zone> getZones(){
        return MappingToResponse.mapToZone();
    }

    /**
     * Получение информации о ячейках конкретной зоны.
     *
     * @param id Идентификатор зоны, для которой запрашиваются ячейки
     * @return Объект CellsOfZone, содержащий информацию о ячейках запрошенной зоны
     * @see CellsOfZone
     */
    @GetMapping(path = "cells/{id}")
    public CellsOfZone getCells(@PathVariable Integer id){
        return MappingToResponse.mapToCells(id);
    }
}
