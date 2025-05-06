package bfg.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import bfg.backend.token.JwtTokenFilter;

/**
 * Конфигурация безопасности приложения.
 * Настраивает аутентификацию на основе JWT токенов и правила доступа к API.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
            "/user/login",
            "/user/refresh",
            "/user/create",
            "/error"
    };

    private static final String[] LOCAL_ENDPOINTS = {
            "/actuator/**"
    };

    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    /**
     * Конфигурация цепочки фильтров безопасности.
     *
     * @param http объект для настройки безопасности
     * @return настроенная цепочка фильтров
     * @throws Exception при ошибках конфигурации
     */
    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключение CSRF защиты, так как используем JWT
                .csrf(AbstractHttpConfigurer::disable)

                // Настройка управления сессией (без сохранения состояния)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Настройка правил авторизации запросов
                .authorizeHttpRequests(auth -> auth
                        // Публичные эндпоинты
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                        .requestMatchers(LOCAL_ENDPOINTS)
                            .access(new WebExpressionAuthorizationManager("hasIpAddress('127.0.0.1') or hasIpAddress('::1')"))

                        // Все остальные запросы требуют аутентификации
                        .anyRequest().authenticated()
                )

                // Добавление JWT фильтра перед стандартным фильтром аутентификации
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}