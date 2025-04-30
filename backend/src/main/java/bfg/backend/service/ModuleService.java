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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
/*
    public void delete(Long idUser, Long id) {
        Optional<User> optionalUser = userRepository.findById(idUser);
        if(optionalUser.isEmpty()){
            throw new RuntimeException("Такого пользователя нет");
        }
        User user = optionalUser.get();
        if(!user.getLive()){
            throw new RuntimeException("Данный пользоваель завершил колнизацию");
        }
        Optional<Module> optionalModule = moduleRepository.findById(id);
        if(optionalModule.isEmpty()){
            throw new RuntimeException("Такого модуля нет");
        }
        Module module = optionalModule.get();
        if(module.getId_user().equals(idUser)){
            moduleRepository.delete(module);
            productionService.recountingProduction(idUser, moduleRepository, linkRepository, resourceRepository);
        }
        else {
            throw new RuntimeException("Такого модуля нет");
        }
    }*/

    public CreatedModule create(Module module) {
        // Получаем аутентификацию из контекста
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Логин пользователя
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException();
        }
        User user = optionalUser.get();

        if(!checkPlaceService.check(new ModulePlace(module.getModule_type(),
                module.getX(), module.getY(), module.getId_zone())).possible()){
            throw new CannotBePutException();
        }

        if(!user.getLive()){
            throw new ColonizationIsCompletedException();
        }

        module.setId_user(user.getId());
        Module resm = moduleRepository.save(module);

        productionService.recountingProduction(resm.getId_user(), moduleRepository, linkRepository, resourceRepository);

        Optional<Resource> optionalResource = resourceRepository.findById(new Resource.PrimaryKey(TypeResources.MATERIAL.ordinal(), resm.getId_user()));
        if(optionalResource.isEmpty()){
            throw new NotResourceException();
        }
        Resource mat = optionalResource.get();
        long cost = TypeModule.values()[resm.getModule_type()].getCost();
        mat.setCount(mat.getCount() - cost);
        if(mat.getCount() < 0){
            user.setLive(false);
            userRepository.save(user);
        }

        return new CreatedModule(resm.getId(), cost);
    }

    public List<Optimality> getOptimality(){
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

        List<Optimality> optimalityList = new ArrayList<>(modules.size());
        for (int i = 0; i < modules.size(); i++) {
            Component component = TypeModule.values()[modules.get(i).getModule_type()].createModule(modules.get(i));
            Integer relief = component.getRelief();
            Integer rationality = component.getRationality(modules, links, resources);
            optimalityList.add(new Optimality(modules.get(i).getId(), relief, rationality));
        }

        return optimalityList;
    }
}
