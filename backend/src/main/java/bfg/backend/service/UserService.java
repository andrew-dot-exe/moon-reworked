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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Сервис для работы с пользователями и их данными.
 * Обеспечивает аутентификацию, регистрацию и доступ к пользовательской информации.
 */
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

    /**
     * Получает полную информацию о пользователе и его колонии
     *
     * @return объект AllUserInfo с данными пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    public AllUserInfo info(){
        User user = getUser(userRepository);

        List<Module> modules = moduleRepository.findByIdUser(user.getId());
        List<Link> links = linkRepository.findByIdUser(user.getId());

        return MappingToResponse.mapToAllUserInfo(user, modules, links);
    }

    /**
     * Получает статистику колонии пользователя
     *
     * @return объект Statistics с данными
     * @throws UserNotFoundException если пользователь не найден
     */
    public Statistics getStatistics(){
        User user = getUser(userRepository);

        List<Module> modules = moduleRepository.findByIdUser(user.getId());
        List<Resource> resources = resourceRepository.findByIdUser(user.getId());
        resources.sort(Resource::compareTo);

        ResourceData resourceData = extractResourceData(resources);
        List<ZoneProduction> zoneProductions = calculateZoneProductions(modules);

        return buildStatistics(user, resourceData, zoneProductions);
    }

    /**
     * Выполняет аутентификацию пользователя
     *
     * @param userIn данные пользователя для входа
     * @return JWT токены доступа
     * @throws UserNotFoundException если пользователь не найден или пароль неверный
     */
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

    /**
     * Регистрирует нового пользователя
     *
     * @param userIn данные нового пользователя
     * @return JWT токены доступа
     * @throws EmailHasBeenUsedAlreadyException если email уже занят
     */
    @Transactional
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

    /**
     * Обновляет JWT токены
     *
     * @param request запрос с refresh токеном
     * @return новые JWT токены
     * @throws NotRefreshTokenException если токен невалиден
     * @throws UserNotFoundException если пользователь не найден
     */
    public JwtResponse refresh(RefreshRequest request){
        String refreshToken = request.refreshToken();
        // Проверяем, что токен валиден и не отозван
        if (!jwtTokenUtil.isTokenExpired(refreshToken)) {
            String username = jwtTokenUtil.extractUsername(refreshToken);
            Optional<User> optionalUser = userRepository.findByEmail(username);
            if(optionalUser.isEmpty()){
                throw new UserNotFoundException();
            }
            String newAccessToken = jwtTokenUtil.generateToken(optionalUser.get());
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(optionalUser.get());
            return new JwtResponse(newAccessToken, newRefreshToken);
        } else {
            throw new NotRefreshTokenException();
        }
    }

    private ResourceData extractResourceData(List<Resource> resources) {
        List<Long> counts = new ArrayList<>(resources.size());
        List<Long> productions = new ArrayList<>(resources.size());
        List<Long> consumptions = new ArrayList<>(resources.size());

        for (Resource resource : resources) {
            counts.add(resource.getCount());
            productions.add(resource.getSum_production());
            consumptions.add(resource.getSum_consumption());
        }

        return new ResourceData(counts, productions, consumptions);
    }

    private List<ZoneProduction> calculateZoneProductions(List<Module> modules) {
        List<ZoneProduction> zoneProductions = new ArrayList<>(Zones.getLength());

        for (int zoneId = 0; zoneId < Zones.getLength(); zoneId++) {
            ZoneProduction zoneProduction = calculateZoneProduction(modules, zoneId);
            zoneProductions.add(zoneProduction);
        }

        return zoneProductions;
    }

    private ZoneProduction calculateZoneProduction(List<Module> modules, int zoneId) {
        List<Long> production = new ArrayList<Long>(Collections.nCopies(TypeResources.values().length, 0L));
        List<Long> consumption = new ArrayList<Long>(Collections.nCopies(TypeResources.values().length, 0L));

        modules.stream()
                .filter(module -> module.getId_zone() == zoneId)
                .forEach(module -> processModuleProduction(module, modules, production, consumption));

        return new ZoneProduction(zoneId, production, consumption);
    }

    private void processModuleProduction(Module module, List<Module> allModules,
                                         List<Long> production, List<Long> consumption) {
        Component component = TypeModule.values()[module.getModule_type()].createModule(module);
        component.getProduction(allModules, production);
        component.getConsumption(allModules, consumption);
    }

    private Statistics buildStatistics(User user, ResourceData resourceData,
                                       List<ZoneProduction> zoneProductions) {
        int currentDay = user.getCurrent_day();
        int successful = successfulService.getSuccessful().successful();

        return new Statistics(
                currentDay,
                successful,
                resourceData.counts,
                resourceData.productions,
                resourceData.consumptions,
                zoneProductions
        );
    }

    // Вспомогательный класс для хранения данных о ресурсах
    private record ResourceData(List<Long> counts, List<Long> productions, List<Long> consumptions) {}

    public static User getUser(UserRepository userRepository){
        // Получаем аутентификацию из контекста
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Логин пользователя
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException();
        }
        return optionalUser.get();
    }
}
