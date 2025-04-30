package bfg.backend.dto.responce.statistics;

import java.util.List;

/**
 * Data Transfer Object (DTO) для представления производственных показателей зоны.
 * Содержит информацию о производстве и потреблении ресурсов в конкретной зоне.
 *
 * @param id - Уникальный идентификатор зоны
 * @param production - Производство всех ресурсов в области
 * @param consumption - Производство всех ресурсов в области
 */
public record ZoneProduction(Integer id,
                             List<Long> production,
                             List<Long> consumption) {}
