package bfg.backend.dto.responce.checkPlace;

/**
 * DTO с результатом проверки возможности размещения объекта.
 * Содержит информацию о допустимости размещения и параметрах места.
 *
 * @param possible - Флаг возможности размещения
 * @param relief - Показатель рельефа местности для данного модуля
 * @param rationality - Рациональность размещения модуля по индивидуальным критериям
 */
public record CheckedPlace (Boolean possible,
                            Integer relief,
                            Integer rationality){
}
