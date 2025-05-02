package bfg.backend.dto.request.user;

/**
 * Data Transfer Object (DTO) для входящих данных пользователя при регистрации и аутентификации.
 * Содержит основные учетные данные пользователя.
 *
 * @param name - Имя пользователя
 * @param email - Электронная почта пользователя (используется как логин)
 * @param password - Пароль пользователя
 */
public record UserIn(String name,
                     String email,
                     String password) {
}
