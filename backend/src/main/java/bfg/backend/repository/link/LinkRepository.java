package bfg.backend.repository.link;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий для работы со связями (Link) между зонами.
 * Предоставляет методы для доступа к данным связей в базе данных.
 */
public interface LinkRepository extends JpaRepository<Link, Link.PrimaryKey> {
    /**
     * Находит все связи, принадлежащие указанному пользователю.
     * Использует нативный SQL-запрос для выборки данных.
     *
     * @param id_user идентификатор пользователя
     * @return список связей пользователя
     */
    @Query(value = "select * from link where id_user = :id_user", nativeQuery = true)
    List<Link> findByIdUser(Long id_user);
}
