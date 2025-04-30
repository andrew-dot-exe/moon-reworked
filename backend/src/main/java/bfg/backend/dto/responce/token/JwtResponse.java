package bfg.backend.dto.responce.token;

/**
 * Data Transfer Object (DTO) для передачи JWT токенов аутентификации.
 * Содержит access и refresh токены для авторизации пользователя.
 *
 * @param accessToken - токен для авторизации запросов. Срок жизни: 1 час
 * @param refreshToken - токен для обновления access токена. Срок жизни: 3 дня
 */
public record JwtResponse(String accessToken, String refreshToken) {}