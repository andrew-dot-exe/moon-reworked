package bfg.backend.controller;

import bfg.backend.dto.responce.area.CellsOfZone;
import bfg.backend.dto.responce.area.Zone;
import bfg.backend.mapping.MappingToResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "area")
public class AreaController {

    @GetMapping(path = "zones")
    public List<Zone> getZones(){
        return MappingToResponse.mapToZone();
    }

    @GetMapping(path = "cells/{id}")
    public CellsOfZone getCells(@PathVariable Integer id){
        return MappingToResponse.mapToCells(id);
    }
}
