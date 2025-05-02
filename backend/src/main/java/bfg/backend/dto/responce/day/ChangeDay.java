package bfg.backend.dto.responce.day;

import java.util.List;

/**
 * DTO, представляющий результат смены игрового дня.
 * Содержит информацию о выживании колонии и изменениях ресурсов.
 *
 * @param live - Статус выживания колонии
 * @param diffResources - Список изменений ресурсов
 */
public record ChangeDay(Boolean live,
                        List<Long> diffResources) {
}
