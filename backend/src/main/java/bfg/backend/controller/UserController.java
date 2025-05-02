package bfg.backend.controller;

import bfg.backend.dto.request.token.RefreshRequest;
import bfg.backend.dto.request.user.UserIn;
import bfg.backend.dto.responce.allUserInfo.AllUserInfo;
import bfg.backend.dto.responce.statistics.Statistics;
import bfg.backend.dto.responce.token.JwtResponse;
import bfg.backend.service.UserService;
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
     * @return JWT токен для доступа к системе
     * @see UserIn
     * @see JwtResponse
     */
    @PostMapping(path = "login")
    public JwtResponse login(@RequestBody UserIn user){
        return userService.login(user);
    }

    /**
     * Регистрация нового пользователя.
     * @param user объект с данными нового пользователя
     * @return JWT токен для доступа к системе
     * @see UserIn
     * @see JwtResponse
     */
    @PostMapping(path = "create")
    public JwtResponse create(@RequestBody UserIn user){
        return userService.create(user);
    }

    /**
     * Обновление JWT токена.
     * @param request объект с refresh токеном
     * @return новая пара access и refresh токенов
     * @see RefreshRequest
     * @see JwtResponse
     */
    @PostMapping("refresh")
    public JwtResponse refreshToken(@RequestBody RefreshRequest request) {
        return userService.refresh(request);
    }
}