package bfg.backend.service;

import bfg.backend.dto.responce.exception.UserNotFoundException;
import bfg.backend.dto.responce.successful.Successful;
import bfg.backend.repository.module.Module;
import bfg.backend.repository.module.ModuleRepository;
import bfg.backend.repository.resource.Resource;
import bfg.backend.repository.resource.ResourceRepository;
import bfg.backend.repository.user.User;
import bfg.backend.repository.user.UserRepository;
import bfg.backend.service.logic.Constants;
import bfg.backend.service.logic.TypeModule;
import bfg.backend.service.logic.TypeResources;
import bfg.backend.service.logic.zones.Zones;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static bfg.backend.service.logic.Constants.DAYS_DELIVERY;
import static bfg.backend.service.logic.Constants.MASS;

/**
 * Сервис для расчета показателей успешности колонии.
 * Вычисляет комплексную оценку состояния колонии на основе различных факторов.
 */
@Service
public class SuccessfulService {

    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final ResourceRepository resourceRepository;

    public SuccessfulService(UserRepository userRepository, ModuleRepository moduleRepository, ResourceRepository resourceRepository) {
        this.userRepository = userRepository;
        this.moduleRepository = moduleRepository;
        this.resourceRepository = resourceRepository;
    }

    /**
     * Рассчитывает комплексные показатели успешности колонии
     *
     * @return объект Successful с показателями успешности
     * @throws UserNotFoundException если пользователь не найден
     */
    public Successful getSuccessful() {
        User user = UserService.getUser(userRepository);

        List<Module> modules = getSortedUserModules(user);
        List<Resource> resources = getSortedUserResources(user);

        int statResources = calculateResourceStability(resources);
        int countPeople = calculatePopulation(modules);

        ModuleStats stats = countModuleStats(modules, countPeople);

        int mood = calculateMood(stats, countPeople, resources, statResources);
        int central = calculateCentralization(stats);
        int search = calculateResearchProgress(stats);

        int successful = calculateOverallSuccess(
                mood,
                stats.needCountPeople,
                countPeople,
                statResources,
                central,
                search
        );

        return new Successful(
                successful,
                mood,
                countPeople,
                stats.needCountPeople,
                statResources,
                central,
                search
        );
    }

    private List<Module> getSortedUserModules(User user) {
        List<Module> modules = moduleRepository.findByIdUser(user.getId());
        modules.sort(Module::compareTo);
        return modules;
    }

    private List<Resource> getSortedUserResources(User user) {
        List<Resource> resources = resourceRepository.findByIdUser(user.getId());
        resources.sort(Resource::compareTo);
        return resources;
    }

    private int calculateResourceStability(List<Resource> resources) {
        long sumDeficit = resources.stream()
                .mapToLong(r -> Math.max(0, r.getConsumption() - r.getProduction()))
                .sum();
        return Math.toIntExact(100 - (sumDeficit * DAYS_DELIVERY) / MASS * 100);
    }

    private int calculatePopulation(List<Module> modules) {
        return (int) modules.stream()
                .filter(m -> m.getModule_type() == TypeModule.LIVE_MODULE_Y.ordinal() ||
                        m.getModule_type() == TypeModule.LIVE_MODULE_X.ordinal())
                .count() * Constants.COUNT_PEOPLE_IN_LIVE_MODULE;
    }

    private ModuleStats countModuleStats(List<Module> modules, int availablePeople) {
        ModuleStats stats = new ModuleStats();

        for (Module module : modules) {
            TypeModule type = TypeModule.values()[module.getModule_type()];
            stats.needCountPeople += type.getPeople();

            if (stats.needCountPeople > availablePeople) continue;

            switch (type) {
                case ADMINISTRATIVE_MODULE:
                case LIVE_ADMINISTRATIVE_MODULE:
                    stats.countA++;
                    break;
                case RESEARCH_MODULE_MINE:
                    stats.mSearch = 1;
                    break;
                case RESEARCH_MODULE_PLANTATION:
                    stats.pSearch = 1;
                    break;
                case RESEARCH_MODULE_TELESCOPE:
                    stats.aSearch = 1;
                    break;
                case RESEARCH_MODULE_TERRITORY:
                    stats.lSearch = 1;
                    break;
                case MEDICAL_MODULE:
                    stats.countM++;
                    break;
                case SPORT_MODULE:
                    stats.countS++;
                    break;
            }
        }

        return stats;
    }

    private int calculateMood(ModuleStats stats, int countPeople,
                              List<Resource> resources, int statResources) {
        if (countPeople == 0) return 0;

        double foodRatio = resources.get(TypeResources.FOOD.ordinal()).getProduction() /
                (resources.get(TypeResources.FOOD.ordinal()).getConsumption() * 0.3);

        return (int) (Math.min((stats.countM + stats.countS) * 3 *
                Constants.COUNT_PEOPLE_IN_LIVE_MODULE / countPeople * 25, 50) +
                (statResources + Math.min(foodRatio, 100)) / 4);
    }

    private int calculateCentralization(ModuleStats stats) {
        return Math.min(stats.countA / Zones.getLength() * 100, 100);
    }

    private int calculateResearchProgress(ModuleStats stats) {
        return Math.min((stats.pSearch + stats.mSearch + stats.lSearch + stats.aSearch) * 25, 100);
    }

    private int calculateOverallSuccess(int mood, int neededPeople, int availablePeople,
                                        int statResources, int central, int search) {
        int populationScore = 100 - Math.max(neededPeople - availablePeople, 0) * 15;
        return (int) (mood * 0.19 + populationScore * 0.21 + statResources * 0.21 +
                central * 0.21 + search * 0.18);
    }

    // Вспомогательный класс для хранения статистики по модулям
    private static class ModuleStats {
        int countA = 0;
        int pSearch = 0;
        int mSearch = 0;
        int lSearch = 0;
        int aSearch = 0;
        int countM = 0;
        int countS = 0;
        int needCountPeople = 0;
    }
}
