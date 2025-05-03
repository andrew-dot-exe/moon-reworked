package bfg.backend.service;

import bfg.backend.dto.request.module.CreatedModule;
import bfg.backend.dto.request.modulePlace.ModulePlace;
import bfg.backend.dto.responce.exception.CannotBePutException;
import bfg.backend.dto.responce.exception.ColonizationIsCompletedException;
import bfg.backend.dto.responce.exception.NotResourceException;
import bfg.backend.dto.responce.exception.UserNotFoundException;
import bfg.backend.dto.responce.optimality.Optimality;
import bfg.backend.repository.link.Link;
import bfg.backend.repository.link.LinkRepository;
import bfg.backend.repository.module.Module;
import bfg.backend.repository.module.ModuleRepository;
import bfg.backend.repository.resource.Resource;
import bfg.backend.repository.resource.ResourceRepository;
import bfg.backend.repository.user.User;
import bfg.backend.repository.user.UserRepository;
import bfg.backend.service.logic.Component;
import bfg.backend.service.logic.TypeModule;
import bfg.backend.service.logic.TypeResources;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления модулями колонии.
 * Обеспечивает создание модулей и анализ их оптимальности.
 */
@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final LinkRepository linkRepository;
    private final ResourceRepository resourceRepository;

    private final ProductionService productionService;
    private final CheckPlaceService checkPlaceService;

    public ModuleService(ModuleRepository moduleRepository, UserRepository userRepository, LinkRepository linkRepository, ResourceRepository resourceRepository, ProductionService productionService, CheckPlaceService checkPlaceService) {
        this.moduleRepository = moduleRepository;
        this.userRepository = userRepository;
        this.linkRepository = linkRepository;
        this.resourceRepository = resourceRepository;
        this.productionService = productionService;
        this.checkPlaceService = checkPlaceService;
    }

    /**
     * Создает новый модуль в указанной зоне
     *
     * @param module данные нового модуля
     * @return Индентификатор нового модуля
     * @throws UserNotFoundException если пользователь не найден
     * @throws CannotBePutException если нельзя разместить модуль в указанном месте
     * @throws ColonizationIsCompletedException если колонизация завершена
     * @throws NotResourceException если недостаточно ресурсов
     */
    @Transactional
    public Long create(Module module) {
        User user = validateUserAndColony();

        validateModulePlacement(module);

        module.setId_user(user.getId());
        Module resm = moduleRepository.save(module);

        productionService.recountingProduction(resm.getId_user(), moduleRepository, linkRepository);

        deductMaterialCost(user, module);

        return resm.getId();
    }

    /**
     * Получает информацию об оптимальности размещения всех модулей
     *
     * @return список показателей оптимальности для каждого модуля
     * @throws UserNotFoundException если пользователь не найден
     */
    public List<Optimality> getOptimality(){
        User user = UserService.getUser(userRepository);

        List<Module> modules = moduleRepository.findByIdUser(user.getId());
        List<Link> links = linkRepository.findByIdUser(user.getId());
        List<Resource> resources = resourceRepository.findByIdUser(user.getId());

        List<Optimality> optimalityList = new ArrayList<>(modules.size());
        for (int i = 0; i < modules.size(); i++) {
            Component component = TypeModule.values()[modules.get(i).getModule_type()].createModule(modules.get(i));
            Integer relief = component.getRelief();
            Integer rationality = component.getRationality(modules, links, resources);
            optimalityList.add(new Optimality(modules.get(i).getId(), relief, rationality));
        }

        return optimalityList;
    }

    /**
     * Проверяет существование пользователя и статус колонии.
     * @throws ColonizationIsCompletedException если колония завершена
     */
    private User validateUserAndColony() {
        User user = UserService.getUser(userRepository);
        if (!user.getLive()) {
            throw new ColonizationIsCompletedException();
        }
        return user;
    }

    /**
     * Проверяет возможность размещения модуля.
     * @throws CannotBePutException если размещение невозможно
     */
    private void validateModulePlacement(Module module) {
        ModulePlace place = new ModulePlace(
                module.getModule_type(),
                module.getX(),
                module.getY(),
                module.getId_zone()
        );

        if (!checkPlaceService.check(place).possible()) {
            throw new CannotBePutException();
        }
    }

    /**
     * Вычитает стоимость модуля из ресурсов.
     * @throws NotResourceException если ресурс не найден
     */
    private void deductMaterialCost(User user, Module module) {
        Resource mat = resourceRepository.findById(new Resource.PrimaryKey(
                        TypeResources.MATERIAL.ordinal(),
                        module.getId_user()
                ))
                .orElseThrow(NotResourceException::new);

        long cost = TypeModule.values()[module.getModule_type()].getCost();
        mat.setCount(mat.getCount() - cost);

        if (mat.getCount() < 0) {
            user.setLive(false);
            userRepository.save(user);
        }
    }
}
