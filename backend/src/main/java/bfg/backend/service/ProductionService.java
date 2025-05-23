package bfg.backend.service;

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
import bfg.backend.service.logic.zones.Zones;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для пересчета производства и потребления ресурсов колонии.
 * Вычисляет суммарные показатели производства и потребления для всех модулей и связей.
 */
@Service
public class ProductionService {

    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;

    ProductionService(UserRepository userRepository, ResourceRepository resourceRepository) {
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
    }

    /**
     * Пересчитывает производство и потребление ресурсов для указанного пользователя
     *
     * @param idUser идентификатор пользователя
     * @param moduleRepository репозиторий модулей
     * @param linkRepository репозиторий связей
     */
    @Transactional
    public void recountingProduction(Long idUser, ModuleRepository moduleRepository,
                                     LinkRepository linkRepository){
        List<Module> modules = moduleRepository.findByIdUser(idUser);
        List<Link> links = linkRepository.findByIdUser(idUser);
        List<Resource> resources = resourceRepository.findByIdUser(idUser);
        resources.sort(Resource::compareTo);

        List<Long> production = new ArrayList<>(TypeResources.values().length);
        List<Long> consumption = new ArrayList<>(TypeResources.values().length);
        fillProduct(production, consumption, modules);

        for (int i = 0; i < resources.size(); i++) {
            resources.get(i).setProduction(production.get(i));
            resources.get(i).setConsumption(consumption.get(i) + (i == TypeResources.WT.ordinal() ? countingWTByLinks(links) : 0L));
        }

        resourceRepository.saveAll(resources);
    }

    /**
     * Находит и возвращает ресурсы пользователя
     * @return Список ресурсов с их количеством, производством и типом
     */
    public List<bfg.backend.dto.responce.allUserInfo.Resource> getResources(){
        User user = UserService.getUser(userRepository);

        List<Resource> resources = resourceRepository.findByIdUser(user.getId());
        List<bfg.backend.dto.responce.allUserInfo.Resource> res = new ArrayList<>(resources.size());
        resources.forEach(e -> res.add(new bfg.backend.dto.responce.allUserInfo.Resource(e)));
        return res;
    }

    /**
     * Заполняет списки в соответсвии с производством/потреблением модулями.
     * @param production Список суммарного производства всех ресурсов
     * @param consumption Список суммарного потребления всех ресурсов
     * @param modules Список модулей
     */
    private void fillProduct(List<Long> production, List<Long> consumption, List<Module> modules){
        for (int j = 0; j < TypeResources.values().length; j++) {
            production.add(0L);
            consumption.add(0L);
        }

        modules.sort(Module::compareTo);
        for(Module mod : modules){
            Component component = TypeModule.values()[mod.getModule_type()].createModule(mod);
            component.getProduction(modules, production);
            component.getConsumption(modules, consumption);
        }
    }

    /**
     * Подсчет потребления электроэнергии маршрутами между областей
     * @param links Список связей между областями
     * @return Суммарное потребление
     */
    private long countingWTByLinks(List<Link> links){
        long consWt = 0L;
        for(Link link : links){
            if(link.getPrimaryKey().getType() == 1){
                consWt += Zones.getZones().get(link.getPrimaryKey().getId_zone1()).getWays()[link.getPrimaryKey().getId_zone2()];
            }
        }
        return consWt * 12L / 10000;
    }
}
