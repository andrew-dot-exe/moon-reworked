package bfg.backend.dto.responce.area;

/**
 * Data Transfer Object (DTO) для представления области.
 * Содержит основные характеристики и параметры зоны.
 *
 * @param id - Номер области
 * @param name - Название области
 * @param widthSecond - Широта в секундах, где расположена область
 * @param longitudeSecond - Долгота в секундах, где расположена область
 * @param illumination - Освещенность области
 * @param ways - Массив расстояний от данной области к другим
 */
public record Zone(Integer id,
                   String name,
                   Integer widthSecond,
                   Integer longitudeSecond,
                   Integer illumination,
                   int[] ways) {
}
