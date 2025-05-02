package bfg.backend.dto.responce.successful;

/**
 * Data Transfer Object (DTO) для представления показателей успешности колонии.
 * Содержит комплексную оценку различных аспектов жизнедеятельности колонии.
 *
 * @param successful - Общий показатель успешности колонии
 * @param mood - Уровень настроения колонистов
 * @param contPeople - Текущее количество колонистов
 * @param needContPeople - Необходимое количество колонистов для оптимальной работы колонии
 * @param resources - Степень автономности колонии от Земли
 * @param central - Централизация колонии
 * @param search - Тэмп исследования
 */
public record Successful(Integer successful,
                         Integer mood,
                         Integer contPeople,
                         Integer needContPeople,
                         Integer resources,
                         Integer central,
                         Integer search) {
}
