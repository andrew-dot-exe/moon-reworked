package bfg.backend.service;

import bfg.backend.dto.responce.exception.ColonizationIsCompletedException;
import bfg.backend.dto.responce.exception.LinkHasBeenAlreadyException;
import bfg.backend.dto.responce.exception.NotResourceException;
import bfg.backend.dto.responce.exception.UserNotFoundException;
import bfg.backend.repository.link.Link;
import bfg.backend.repository.link.LinkRepository;
import bfg.backend.repository.resource.Resource;
import bfg.backend.repository.resource.ResourceRepository;
import bfg.backend.repository.user.User;
import bfg.backend.repository.user.UserRepository;
import bfg.backend.service.logic.TypeResources;
import bfg.backend.service.logic.zones.Zones;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Сервис для управления связями между зонами колонии.
 * Обрабатывает создание связей и связанные с этим расходы ресурсов.
 */
@Service
public class LinkService {

    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;

    public LinkService(LinkRepository linkRepository, UserRepository userRepository, ResourceRepository resourceRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
    }

    /**
     * Создает новую связь между зонами и обрабатывает связанные расходы ресурсов
     *
     * @param link создаваемая связь
     * @return стоимость создания связи (в материальных ресурсах)
     * @throws UserNotFoundException если пользователь не найден
     * @throws ColonizationIsCompletedException если колония завершена
     * @throws LinkHasBeenAlreadyException если связь уже существует
     * @throws NotResourceException если не найден требуемый ресурс
     */
    @Transactional
    public Long create(Link link) {
        User user = validateUserAndColony();
        validateLinkExistence(link, user.getId());
        linkRepository.save(link);

        long way = calculateDistanceBetweenZones(link);

        return link.getPrimaryKey().getType() == 0
                ? handleMaterialResource(link, user, way)
                : handleEnergyResource(link, way);
    }

    /**
     * Проверяет существование пользователя и статус колонии.
     * @throws UserNotFoundException если пользователь не найден
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
     * Проверяет, существует ли уже такая связь.
     * @throws LinkHasBeenAlreadyException если связь найдена
     */
    private void validateLinkExistence(Link link, Long userId) {
        link.getPrimaryKey().setId_user(userId);
        if (linkRepository.findById(link.getPrimaryKey()).isPresent()) {
            throw new LinkHasBeenAlreadyException();
        }
    }

    /**
     * Возвращает расстояние между зонами.
     */
    private long calculateDistanceBetweenZones(Link link) {
        return Zones.getZones()
                .get(link.getPrimaryKey().getId_zone1())
                .getWays()[link.getPrimaryKey().getId_zone2()];
    }

    /**
     * Рассчитывает расход материалов на постройку провода.
     * @return стоимость создания связи в граммах
     * @throws NotResourceException если ресурс не найден
     */
    private Long handleMaterialResource(Link link, User user, long way) {
        Resource mat = resourceRepository.findById(new Resource.PrimaryKey(
                        TypeResources.MATERIAL.ordinal(),
                        link.getPrimaryKey().getId_user()
                ))
                .orElseThrow(NotResourceException::new);

        mat.setCount(mat.getCount() - way);
        if (mat.getCount() < 0) {
            user.setLive(false);
            userRepository.save(user);
        }
        resourceRepository.save(mat);
        return way * 1000; // перевод в граммы
    }

    /**
     * Рассчитывает расход энергии на работу маршрута.
     * @throws NotResourceException если ресурс не найден
     */
    private Long handleEnergyResource(Link link, long way) {
        Resource wt = resourceRepository.findById(new Resource.PrimaryKey(
                        TypeResources.WT.ordinal(),
                        link.getPrimaryKey().getId_user()
                ))
                .orElseThrow(NotResourceException::new);

        wt.setConsumption(wt.getConsumption() + way * 12L / 10000);
        resourceRepository.save(wt);
        return 0L;
    }
}
