package bfg.backend.service;

import bfg.backend.dto.responce.allUserInfo.AllUserInfo;
import bfg.backend.dto.responce.exception.ColonizationIsNotCompletedException;
import bfg.backend.dto.responce.exception.UserNotFoundException;
import bfg.backend.mapping.MappingToResponse;
import bfg.backend.repository.link.Link;
import bfg.backend.repository.link.LinkRepository;
import bfg.backend.repository.module.Module;
import bfg.backend.repository.module.ModuleRepository;
import bfg.backend.repository.resource.Resource;
import bfg.backend.repository.resource.ResourceRepository;
import bfg.backend.repository.user.User;
import bfg.backend.repository.user.UserRepository;
import bfg.backend.service.logic.TypeResources;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static bfg.backend.service.logic.Constants.DAYS_DELIVERY;

/**
 * Сервис для управления колонией пользователя.
 * Обеспечивает создание новой колонии и удаление существующей.
 */
@Service
public class ColonyService {

    private final UserRepository userRepository;
    private final LinkRepository linkRepository;
    private final ModuleRepository moduleRepository;
    private final ResourceRepository resourceRepository;

    public ColonyService(UserRepository userRepository, LinkRepository linkRepository, ModuleRepository moduleRepository, ResourceRepository resourceRepository) {
        this.userRepository = userRepository;
        this.linkRepository = linkRepository;
        this.moduleRepository = moduleRepository;
        this.resourceRepository = resourceRepository;
    }

    /**
     * Удаляет колонию пользователя и все связанные данные.
     *
     * @throws UserNotFoundException если пользователь не найден
     */
    @Transactional
    public void delete(){
        User user = UserService.getUser(userRepository);

        List<Link> links = linkRepository.findByIdUser(user.getId());
        List<Module> modules = moduleRepository.findByIdUser(user.getId());
        List<Resource> resources = resourceRepository.findByIdUser(user.getId());

        linkRepository.deleteAll(links);
        moduleRepository.deleteAll(modules);
        resourceRepository.deleteAll(resources);

        user.setLive(false);
        userRepository.save(user);
    }

    /**
     * Создает новую колонию для пользователя.
     *
     * @throws UserNotFoundException если пользователь не найден
     * @throws ColonizationIsNotCompletedException если у пользователя уже есть активная колония
     */
    @Transactional
    public void create(){
        User user = validateUserAndColony();

        user.setLive(true);
        user.setCurrent_day(0);
        user.setDays_before_delivery(DAYS_DELIVERY);

        deleteOld(user);

        updateResources(user.getId());
        userRepository.save(user);
    }

    private User validateUserAndColony(){
        User user = UserService.getUser(userRepository);
        if(user.getLive()){
            throw new ColonizationIsNotCompletedException();
        }
        return user;
    }

    private void deleteOld(User user){
        List<Link> links = linkRepository.findByIdUser(user.getId());
        if(!links.isEmpty()){
            linkRepository.deleteAll(links);
        }
        List<Module> modules = moduleRepository.findByIdUser(user.getId());
        if(!modules.isEmpty()){
            moduleRepository.deleteAll(modules);
        }
    }

    private void updateResources(Long idUser){
        List<Resource> resources = new ArrayList<>();
        for (int i = 0; i < TypeResources.values().length; i++) {
            resources.add(new Resource(new Resource.PrimaryKey(i, idUser), TypeResources.values()[i].getStartCount(), 0L, 0L, 0L, 0L));
        }
        resourceRepository.saveAll(resources);
    }
}
