package bfg.backend.service;

import bfg.backend.dto.request.token.RefreshRequest;
import bfg.backend.dto.request.user.UserIn;
import bfg.backend.dto.responce.allUserInfo.AllUserInfo;
import bfg.backend.dto.responce.exception.EmailHasBeenUsedAlreadyException;
import bfg.backend.dto.responce.exception.NotRefreshTokenException;
import bfg.backend.dto.responce.exception.UserNotFoundException;
import bfg.backend.dto.responce.statistics.Statistics;
import bfg.backend.dto.responce.statistics.ZoneProduction;
import bfg.backend.dto.responce.token.JwtResponse;
import bfg.backend.mapping.MappingToResponse;
import bfg.backend.repository.link.*;
import bfg.backend.repository.module.Module;
import bfg.backend.repository.module.ModuleRepository;
import bfg.backend.repository.resource.*;
import bfg.backend.repository.user.*;
import bfg.backend.service.logic.Component;
import bfg.backend.service.logic.TypeModule;
import bfg.backend.service.logic.TypeResources;
import bfg.backend.service.logic.zones.Zones;
import bfg.backend.token.JwtTokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LinkRepository linkRepository;
    private final ModuleRepository moduleRepository;
    private final ResourceRepository resourceRepository;
    private final SuccessfulService successfulService;

    private final JwtTokenUtil jwtTokenUtil;

    public UserService(UserRepository userRepository, LinkRepository linkRepository, ModuleRepository moduleRepository, ResourceRepository resourceRepository, SuccessfulService successfulService, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.linkRepository = linkRepository;
        this.moduleRepository = moduleRepository;
        this.resourceRepository = resourceRepository;
        this.successfulService = successfulService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public AllUserInfo info(){
        // Получаем аутентификацию из контекста
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Логин пользователя
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            return null;
        }
        User user = optionalUser.get();

        List<Module> modules = moduleRepository.findByIdUser(user.getId());
        List<Link> links = linkRepository.findByIdUser(user.getId());
        List<Resource> resources = resourceRepository.findByIdUser(user.getId());

        return MappingToResponse.mapToAllUserInfo(user, modules, links, resources);
    }

    public Statistics getStatistics(){
        // Получаем аутентификацию из контекста
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Логин пользователя
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            return null;
        }
        User user = optionalUser.get();

        List<Module> modules = moduleRepository.findByIdUser(user.getId());
        List<Resource> resources = resourceRepository.findByIdUser(user.getId());
        resources.sort(Resource::compareTo);

        List<Long> count = new ArrayList<>(resources.size());
        List<Long> sproduction = new ArrayList<>(resources.size());
        List<Long> sconsumption = new ArrayList<>(resources.size());
        List<ZoneProduction> zoneProductions = new ArrayList<>(Zones.getLength());

        for (Resource resource : resources){
            count.add(resource.getCount());
            sproduction.add(resource.getSum_production());
            sconsumption.add(resource.getSum_consumption());
        }

        for (int i = 0; i < Zones.getLength(); i++) {
            List<Long> production = new ArrayList<>(TypeResources.values().length);
            List<Long> consumption = new ArrayList<>(TypeResources.values().length);
            for (int j = 0; j < TypeResources.values().length; j++) {
                production.add(0L);
                consumption.add(0L);
            }
            for(Module module : modules){
                Component component = TypeModule.values()[module.getModule_type()].createModule(module);
                component.getProduction(i, modules, production);
                component.getConsumption(i, modules, consumption);
            }
            zoneProductions.add(new ZoneProduction(i, production, consumption));
        }

        return new Statistics(user.getCurrent_day(), successfulService.getSuccessful().successful(), count, sproduction, sconsumption, zoneProductions);
    }

    public JwtResponse login(UserIn userIn) {
        Optional<User> optionalUser = userRepository.findByEmail(userIn.email());
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException();
        }
        User user = optionalUser.get();
        if(!user.checkPassword(userIn.password())){
            throw new UserNotFoundException();
        }
        String accessToken = jwtTokenUtil.generateToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);

        return new JwtResponse(accessToken, refreshToken);
    }

    public JwtResponse create(UserIn userIn) {
        Optional<User> optionalUser = userRepository.findByEmail(userIn.email());
        if(optionalUser.isPresent()){
            throw new EmailHasBeenUsedAlreadyException();
        }
        User user = new User(null, userIn.name(), userIn.email(), userIn.password(), 0, 0, false);
        userRepository.save(user);
        String accessToken = jwtTokenUtil.generateToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);

        return new JwtResponse(accessToken, refreshToken);
    }

    public JwtResponse refresh(RefreshRequest request){
        String refreshToken = request.refreshToken();

        // Проверяем, что токен валиден и не отозван
        if (jwtTokenUtil.isTokenExpired(refreshToken)) {
            String username = jwtTokenUtil.extractUsername(refreshToken);
            Optional<User> optionalUser = userRepository.findByEmail(username);
            if(optionalUser.isEmpty()){
                throw new UserNotFoundException();
            }
            String newAccessToken = jwtTokenUtil.generateToken(optionalUser.get());
            return new JwtResponse(newAccessToken, refreshToken);
        } else {
            throw new NotRefreshTokenException();
        }
    }
}
