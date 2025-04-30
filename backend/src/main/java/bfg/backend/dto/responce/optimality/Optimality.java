package bfg.backend.dto.responce.optimality;

/**
 * Data Transfer Object (DTO) для передачи данных об оптимальности размещения модуля.
 * Содержит показатели оценки рельефа и рациональности размещения.
 *
 * @param id - Уникальный идентификатор модуля
 * @param relief - Показатель рельефа местности
 * @param rationality - Рациональность модуля
 */
public record Optimality(Long id,
                         Integer relief,
                         Integer rationality) {
}
