package bfg.backend.dto.responce.moduleType;

/**
 * Хранит характеристики конкретного модуля.
 *
 * @param type индентификационный номер типа модуля
 * @param name название модуля
 * @param people количество людей, необходимых для его функционирования
 * @param cost стоимость постройки в материалах
 * @param live флаг - жилой ли модуль
 */
public record ModuleType(Integer type,
                         String name,
                         Integer people,
                         Integer cost,
                         Boolean live) {
}
