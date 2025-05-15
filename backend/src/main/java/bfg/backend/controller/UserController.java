package bfg.backend.controller;

import bfg.backend.dto.request.token.RefreshRequest;
import bfg.backend.dto.request.user.UserIn;
import bfg.backend.dto.responce.allUserInfo.AllUserInfo;
import bfg.backend.dto.responce.statistics.Statistics;
import bfg.backend.dto.responce.token.JwtResponse;
import bfg.backend.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы с пользователями системы.
 * Обеспечивает аутентификацию, регистрацию и получение пользовательских данных.
 */
@RestController
@RequestMapping(path = "user")
public class UserController {

    private final UserService userService;

    public UserController(UserService findUserService) {
        this.userService = findUserService;
    }

    /**
     * Получение статистики пользователя.
     * @return объект со статистикой колонии пользователя
     * @see Statistics
     */
    @GetMapping(path = "statistic")
    public Statistics statistics(){
        return userService.getStatistics();
    }

    /**
     * Получение полной информации колонии пользователя.
     * @return объект со всеми данными об колонии
     * @see AllUserInfo
     */
    @GetMapping(path = "info")
    public AllUserInfo info(){
        return userService.info();
    }

    /**
     * Аутентификация пользователя в системе.
     * @param user объект с учетными данными пользователя
     * @return JWT токен для доступа к системе в cookies
     * @see UserIn
     * @see String
     */
@PostMapping(path = "login")
public String login(@RequestBody UserIn user, HttpServletResponse response){
    JwtResponse jwtResponse = userService.login(user);
    
    // Cookie для access token
    Cookie tokenCookie = new Cookie("token", jwtResponse.accessToken());
    tokenCookie.setHttpOnly(true);
    tokenCookie.setPath("/");
    tokenCookie.setMaxAge(3600);
    //tokenCookie.setSecure(true); // Раскомментировать для HTTPS
    
    response.addCookie(tokenCookie);
    
    // Cookie для refresh token
    Cookie refreshCookie = new Cookie("refresh_token", jwtResponse.refreshToken());
    refreshCookie.setHttpOnly(true);
    refreshCookie.setPath("/");
    refreshCookie.setMaxAge(259200); // 3 дня
    //refreshCookie.setSecure(true); // Раскомментировать для HTTPS
    
    response.addCookie(refreshCookie);
    
    return "logged in";
}

    /**
     * Регистрация нового пользователя.
     * @param user объект с данными нового пользователя
     * @return JWT токен для доступа к системе
     * @see UserIn
     * @see JwtResponse
     */
    @PostMapping(path = "create")
    public String create(@RequestBody UserIn user, HttpServletResponse response){
        JwtResponse JwtResponse = userService.create(user);
        
        // Cookie для access token
        Cookie tokenCookie = new Cookie("token", JwtResponse.accessToken());
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(3600);
        //tokenCookie.setSecure(true); // Раскомментировать для HTTPS
        
        response.addCookie(tokenCookie);
        
        // Cookie для refresh token
        Cookie refreshCookie = new Cookie("refresh_token", JwtResponse.refreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(259200); // 3 дня
        //refreshCookie.setSecure(true); // Раскомментировать для HTTPS
        
        response.addCookie(refreshCookie);
        
        return "registered";
    }

    /**
     * Выход пользователя из системы.
     * @return сообщение об успешном выходе
     */
    @PostMapping(path = "logout")
    public String logout(HttpServletResponse response) {
        // Удаляем cookie с токеном, устанавливая время жизни = 0
        Cookie tokenCookie = new Cookie("token", null);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(0);
        response.addCookie(tokenCookie);
        
        // Удаляем cookie с refresh токеном
        Cookie refreshCookie = new Cookie("refresh_token", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);
        
        // Очищаем контекст безопасности
        SecurityContextHolder.clearContext();
        
        return "logged out";
    }

    /**
     * Обновление JWT токена.
     * @param request объект с refresh токеном
     * @return новая пара access и refresh токенов
     * @see RefreshRequest
     * @see JwtResponse
     */
    @PostMapping("refresh")
    public String refreshToken(@CookieValue("refresh_token") String refreshToken,
                               HttpServletResponse response) {
        JwtResponse jwtResponse = userService.refresh(new RefreshRequest(refreshToken));

        // Обновляем токен в cookie
        Cookie tokenCookie = new Cookie("token", jwtResponse.accessToken());
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(3600); // 1 час
        //tokenCookie.setSecure(true); // Раскомментировать для HTTPS

        response.addCookie(tokenCookie);

        // Обновляем refresh токен (по желанию можно сохранить в отдельную cookie)
        Cookie refreshCookie = new Cookie("refresh_token", jwtResponse.refreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(259200); // 3 дня
        //refreshCookie.setSecure(true); // Раскомментировать для HTTPS

        response.addCookie(refreshCookie);

        return "tokens_refreshed";
    }
}