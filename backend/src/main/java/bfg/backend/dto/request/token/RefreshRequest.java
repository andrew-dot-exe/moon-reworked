package bfg.backend.dto.request.token;

/**
 * Data Transfer Object (DTO) для запроса обновления JWT токена.
 * Содержит refresh token, используемый для получения новой пары access/refresh токенов.
 *
 * @param refreshToken токен обновления, выданный при предыдущей аутентификации,
 *                    необходимый для получения новых авторизационных токенов.
 */
public record RefreshRequest(
        String refreshToken
) {}