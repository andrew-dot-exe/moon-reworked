package bfg.backend.service;

import bfg.backend.dto.responce.allUserInfo.AllUserInfo;
import bfg.backend.dto.responce.exception.ColonizationIsNotCompletedException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static bfg.backend.service.logic.Constants.DAYS_DELIVERY;

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

    public void delete(){
        // Получаем аутентификацию из контекста
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Логин пользователя
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            return;
        }
        User user = optionalUser.get();

        List<Link> links = linkRepository.findByIdUser(user.getId());
        List<Module> modules = moduleRepository.findByIdUser(user.getId());
        List<Resource> resources = resourceRepository.findByIdUser(user.getId());

        linkRepository.deleteAll(links);
        moduleRepository.deleteAll(modules);
        resourceRepository.deleteAll(resources);

        user.setLive(false);
        userRepository.save(user);
    }

    public void create(){
        // Получаем аутентификацию из контекста
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Логин пользователя
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            return;
        }
        User user = optionalUser.get();
        if(user.getLive()){
            throw new ColonizationIsNotCompletedException();
        }

        user.setLive(true);
        user.setCurrent_day(0);
        user.setDays_before_delivery(DAYS_DELIVERY);

        List<Resource> resources = new ArrayList<>();
        for (int i = 0; i < TypeResources.values().length; i++) {
            resources.add(new Resource(new Resource.PrimaryKey(i, user.getId()), TypeResources.values()[i].getStartCount(), 0L, 0L, 0L, 0L));
        }
        resourceRepository.saveAll(resources);

    }
}
