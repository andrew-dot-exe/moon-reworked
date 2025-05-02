package bfg.backend.service;

import bfg.backend.dto.responce.day.ChangeDay;
import bfg.backend.dto.responce.exception.ColonizationIsCompletedException;
import bfg.backend.dto.responce.exception.UserNotFoundException;
import bfg.backend.repository.resource.Resource;
import bfg.backend.repository.resource.ResourceRepository;
import bfg.backend.repository.user.User;
import bfg.backend.repository.user.UserRepository;
import bfg.backend.service.logic.TypeModule;
import bfg.backend.service.logic.TypeResources;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static bfg.backend.service.logic.Constants.*;

/**
 * Сервис для обработки игровых дней и связанных с ними процессов.
 * Отвечает за переход на следующий день, расчет ресурсов и проверку выживания колонии.
 */
@Service
public class DayService {

    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;

    public DayService(UserRepository userRepository, ResourceRepository resourceRepository) {
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
    }

    /**
     * Переводит колонию на следующий игровой день.
     * Выполняет расчеты ресурсов, проверяет условия выживания.
     *
     * @return DTO с результатами перехода на новый день
     * @throws UserNotFoundException если пользователь не найден
     * @throws ColonizationIsCompletedException если колонизация уже завершена
     */
    @Transactional
    public ChangeDay addDay(){
        // Получаем аутентификацию из контекста
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Логин пользователя
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException();
        }
        User user = optionalUser.get();
        if(!user.getLive()){
            throw new ColonizationIsCompletedException();
        }

        user.setCurrent_day(user.getCurrent_day() + 1);
        boolean delivery = user.getDays_before_delivery() == 1;
        if(delivery) user.setDays_before_delivery(DAYS_DELIVERY);
        else user.setDays_before_delivery(user.getDays_before_delivery() - 1);

        List<Resource> resources = resourceRepository.findByIdUser(user.getId());
        resources.sort(Resource::compareTo);

        // Проверка на достаток кислорода
        long d = resources.get(TypeResources.O2.ordinal()).getConsumption() - resources.get(TypeResources.O2.ordinal()).getProduction();
        if(d > 0){
            resources.get(TypeResources.WT.ordinal()).setConsumption(d * WT_FOR_KG_O2 / 1000 + resources.get(TypeResources.WT.ordinal()).getConsumption());
            resources.get(TypeResources.H2O.ordinal()).setConsumption((long) (d * H2O_FOR_KG_O2) + resources.get(TypeResources.H2O.ordinal()).getConsumption());
            resources.get(TypeResources.O2.ordinal()).setProduction(d + resources.get(TypeResources.O2.ordinal()).getProduction());
        }

        List<Long> diffResources = new ArrayList<>(resources.size());
        Long diff;
        for (Resource resource : resources) {
            diff = resource.getProduction() - resource.getConsumption();
            if (delivery && diff < 0 && resource.getPrimaryKey().getResource_type() != TypeResources.WT.ordinal()) {
                diff -= diff * DAYS_DELIVERY + (resource.getCount() + diff * 5);
            }
            diffResources.add(diff);
            resource.setCount(resource.getCount() + diff);
            resource.setSum_production(resource.getSum_production() + resource.getProduction());
            resource.setSum_consumption(resource.getSum_consumption() + resource.getConsumption());
        }

        Boolean live = resources.stream().allMatch(e -> e.getCount() >= 0);
        resourceRepository.saveAll(resources);
        user.setLive(live);
        userRepository.save(user);
        return new ChangeDay(live, diffResources);
    }
}
