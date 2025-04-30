package bfg.backend.dto.responce.allUserInfo;

import java.util.List;

/**
 * Полная информация о пользователе и состоянии его колонии.
 * Содержит все основные данные для отображения в интерфейсе игры.
 *
 * @param name - Имя пользователя
 * @param curDay - Текущий день от начала колонизации
 * @param dayBeforeDelivery - Количество дней до доставки ресурсов с Земли
 * @param live - Наличие не завершенной колонии
 * @param resources - Список с количеством рсурсов и их суммарного производства/потребления
 * @param links - Список связей между областями
 * @param modules - Список построенных модулей в колонии
 */
public record AllUserInfo(String name,
                          Integer curDay,
                          Integer dayBeforeDelivery,
                          Boolean live,
                          List<Resource> resources,
                          List<Link> links,
                          List<Module> modules) {}
