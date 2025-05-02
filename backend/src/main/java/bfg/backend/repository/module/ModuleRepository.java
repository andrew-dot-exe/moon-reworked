package bfg.backend.repository.module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий для работы с модулями.
 * Предоставляет методы для доступа к данным модулей в базе данных.
 */
public interface ModuleRepository extends JpaRepository<Module, Long> {
    /**
     * Находит все модули, принадлежащие указанному пользователю.
     * Использует нативный SQL-запрос для выборки данных.
     *
     * @param id_user идентификатор пользователя
     * @return список модулей пользователя
     */
    @Query(value = "select * from module where id_user = :id_user", nativeQuery = true)
    List<Module> findByIdUser(Long id_user);
}
