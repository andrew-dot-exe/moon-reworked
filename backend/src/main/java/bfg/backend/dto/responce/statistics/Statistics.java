package bfg.backend.dto.responce.statistics;

import java.util.List;

/**
 * Data Transfer Object (DTO) для предоставления статистики колонии.
 * Содержит информацию о продолжительности существования колонии, успешности колонии,
 * ресурсах и производственных показателях.
 *
 * @param countDay - Количество дней колонизации
 * @param successful - Показатель успешности колонии
 * @param countResources - Количество всех ресурсов
 * @param sumProduction - Суммарное количество произведенных ресурсов
 * @param sumConsumption - Суммарное количество потребленых ресурсов
 * @param zoneProductions - Производство и потребление ресурсов в каждой области
 */
public record Statistics(Integer countDay,
                         Integer successful,
                         List<Long> countResources,
                         List<Long> sumProduction,
                         List<Long> sumConsumption,
                         List<ZoneProduction> zoneProductions) {
}
