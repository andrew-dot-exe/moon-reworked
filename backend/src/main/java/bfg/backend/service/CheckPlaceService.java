package bfg.backend.service;

import bfg.backend.dto.request.modulePlace.ModulePlace;
import bfg.backend.dto.responce.checkPlace.CheckedPlace;
import bfg.backend.dto.responce.exception.ColonizationIsCompletedException;
import bfg.backend.dto.responce.exception.UserNotFoundException;
import bfg.backend.repository.link.*;
import bfg.backend.repository.module.Module;
import bfg.backend.repository.module.ModuleRepository;
import bfg.backend.repository.resource.*;
import bfg.backend.repository.user.*;
import bfg.backend.service.logic.Component;
import bfg.backend.service.logic.TypeModule;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для проверки возможности размещения модулей в области.
 * Оценивает рельеф и рациональность размещения нового модуля.
 */
@Service
public class CheckPlaceService {

    private final UserRepository userRepository;
    private final LinkRepository linkRepository;
    private final ModuleRepository moduleRepository;
    private final ResourceRepository resourceRepository;

    public CheckPlaceService(UserRepository userRepository, LinkRepository linkRepository, ModuleRepository moduleRepository, ResourceRepository resourceRepository) {
        this.userRepository = userRepository;
        this.linkRepository = linkRepository;
        this.moduleRepository = moduleRepository;
        this.resourceRepository = resourceRepository;
    }

    /**
     * Проверяет возможность размещения модуля в указанной позиции
     *
     * @param modulePlace DTO с параметрами размещаемого модуля
     * @return результат проверки с показателями рельефа и рациональности
     * @throws UserNotFoundException если пользователь не найден
     * @throws ColonizationIsCompletedException если колонизация завершена
     */
    public CheckedPlace check(ModulePlace modulePlace) {
        User user = validateUser();

        ColonyData colonyData = loadColonyData(user);
        Component component = createComponent(modulePlace, user);

        return calculatePlacementSuitability(component, colonyData);
    }

    /**
     * Проверяет активность пользователя
     * @throws ColonizationIsCompletedException если колония завершена
     */
    private User validateUser() {
        User user = UserService.getUser(userRepository);
        if (!user.getLive()) {
            throw new ColonizationIsCompletedException();
        }
        return user;
    }

    /**
     * Загружает все данные колонии для проверки размещения
     */
    private ColonyData loadColonyData(User user) {
        return new ColonyData(
                moduleRepository.findByIdUser(user.getId()),
                linkRepository.findByIdUser(user.getId()),
                resourceRepository.findByIdUser(user.getId())
        );
    }

    /**
     * Создает компонент для проверки размещения
     */
    private Component createComponent(ModulePlace modulePlace, User user) {
        return TypeModule.values()[modulePlace.typeModule()]
                .createModule(
                        user.getId(),
                        modulePlace.idZone(),
                        modulePlace.x(),
                        modulePlace.y()
                );
    }

    /**
     * Рассчитывает пригодность размещения модуля
     */
    private CheckedPlace calculatePlacementSuitability(
            Component component,
            ColonyData data) {
        Integer relief = component.getRelief();
        Integer rationality = 100;// component.getRationality(data.modules, data.links, data.resources);

        // TODO вернуть нормально
        return new CheckedPlace(
                relief != null && rationality != null,
                relief,
                rationality
        );
    }

    // Вспомогательный класс для группировки данных
    private record ColonyData(List<Module> modules, List<Link> links, List<Resource> resources) {}

}
