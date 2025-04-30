package bfg.backend.controller;

import bfg.backend.dto.request.token.RefreshRequest;
import bfg.backend.dto.request.user.UserIn;
import bfg.backend.dto.responce.allUserInfo.AllUserInfo;
import bfg.backend.dto.responce.statistics.Statistics;
import bfg.backend.dto.responce.token.JwtResponse;
import bfg.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "user")
public class UserController {

    private final UserService userService;

    public UserController(UserService findUserService) {
        this.userService = findUserService;
    }

    @GetMapping(path = "statistic")
    public Statistics statistics(){
        return userService.getStatistics();
    }

    @GetMapping(path = "info")
    public AllUserInfo info(){
        return userService.info();
    }

    @PostMapping(path = "login")
    public JwtResponse login(@RequestBody UserIn user){
        return userService.login(user);
    }

    @PostMapping(path = "create")
    public JwtResponse create(@RequestBody UserIn user){
        return userService.create(user);
    }

    @PostMapping("refresh")
    public JwtResponse refreshToken(@RequestBody RefreshRequest request) {
        return userService.refresh(request);
    }

}
