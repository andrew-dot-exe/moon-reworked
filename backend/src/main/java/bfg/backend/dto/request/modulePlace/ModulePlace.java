package bfg.backend.dto.request.modulePlace;

/**
 * Data Transfer Object (DTO) для размещения модуля на игровой площадке.
 * Содержит информацию о положении и типе размещаемого модуля.
 * @param typeModule - Тип размещаемого модуля
 * @param x - Координата X для размещения модуля на игровой сетке
 * @param y - Координата Y для размещения модуля на игровой сетке
 * @param idZone - Идентификатор зоны, в которой размещается модуль
 */
public record ModulePlace(Integer typeModule,
                          Integer x,
                          Integer y,
                          Integer idZone) {}
