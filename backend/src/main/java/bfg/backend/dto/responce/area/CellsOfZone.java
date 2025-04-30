package bfg.backend.dto.responce.area;

import bfg.backend.service.logic.zones.Cell;

/**
 * Data Transfer Object (DTO) для представления матрицы ячеек зоны.
 * Содержит двумерный массив ячеек, представляющий кокретную область.
 */
public record CellsOfZone(Cell[][] cells) {
}
