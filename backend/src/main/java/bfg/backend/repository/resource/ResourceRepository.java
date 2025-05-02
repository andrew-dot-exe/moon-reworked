package bfg.backend.repository.resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий для работы с ресурсами.
 * Предоставляет методы для доступа к данным ресурсов в базе данных.
 */
public interface ResourceRepository extends JpaRepository<Resource, Resource.PrimaryKey> {
    /**
     * Находит все ресурсы, принадлежащие указанному пользователю.
     * Использует нативный SQL-запрос для выборки данных.
     *
     * @param id_user идентификатор пользователя
     * @return список ресурсов пользователя
     */
    @Query(value = "select * from resource where id_user = :id_user", nativeQuery = true)
    List<Resource> findByIdUser(Long id_user);
}
