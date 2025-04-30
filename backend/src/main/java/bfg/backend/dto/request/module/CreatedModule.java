package bfg.backend.dto.request.module;

/**
 * Data Transfer Object (DTO) для представления созданного модуля.
 * Содержит идентификатор и количество затраченных материалов созданного модуля.
 *
 * @param id уникальный идентификатор модуля в системе
 * @param cost количество затраченных материалов на создание модуля
 */
public record CreatedModule(Long id, Long cost) {
}