package bfg.backend.repository.link;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

/**
 * Сущность, представляющая связь между зонами в системе.
 * Использует составной первичный ключ {@link PrimaryKey}.
 */
@Entity
public class Link {

    @EmbeddedId
    private PrimaryKey primaryKey;

    /**
     * Статический класс для составного первичного ключа связи.
     * Содержит тип связи, идентификатор пользователя и идентификаторы связанных зон.
     */
    public static class PrimaryKey{
        /**
         * Тип связи:
         * 0 - Электрический кабель
         * 1 - Маршрут
         */
        private Integer type;
        private Long id_user;
        private Integer id_zone1;
        private Integer id_zone2;

        public PrimaryKey(Integer type, Long id_user, Integer id_zone1, Integer id_zone2) {
            this.type = type;
            this.id_user = id_user;
            this.id_zone1 = id_zone1;
            this.id_zone2 = id_zone2;
        }

        public PrimaryKey() {}

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Long getId_user() {
            return id_user;
        }

        public void setId_user(Long id_user) {
            this.id_user = id_user;
        }

        public Integer getId_zone1() {
            return id_zone1;
        }

        public void setId_zone1(Integer id_zone1) {
            this.id_zone1 = id_zone1;
        }

        public Integer getId_zone2() {
            return id_zone2;
        }

        public void setId_zone2(Integer id_zone2) {
            this.id_zone2 = id_zone2;
        }
    }

    public Link(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Link() {}

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }
}
