package bfg.backend.dto.responce.area;

import bfg.backend.service.logic.zones.Cell;

public record CellsOfZone(Cell[][] cells) {
}
