package bfg.backend.service.logic;

import bfg.backend.repository.link.Link;
import bfg.backend.repository.module.Module;
import bfg.backend.repository.resource.Resource;
import bfg.backend.service.logic.zones.Zones;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static bfg.backend.service.logic.Constants.*;
import static bfg.backend.service.logic.Constants.DANGER_ZONE;

/**
 * Интерфейс, определяющий поведение компонентов (модулей) колонии.
 * Предоставляет методы расчета различных свойств и взаимодействий между модулями.
 */
public interface Component {

    /**
     * Рассчитывает пригодность рельефа местности для данного компонента.
     * @return оценка рельефа (0-100) или null, если полностью не пригоден
     */
    Integer getRelief();

    /**
     * Вычисляет оценку рациональности размещения для этого компонента.
     * @param modules - Список всех модулей
     * @param links - Список всех связей между областями
     * @param resources - Список ресурсов
     * @return Оценку рациональности (0-100) или null, если не возможно поставить из-за ряда причин
     */
    Integer getRationality(List<Module> modules, List<Link> links, List<Resource> resources);

    /**
     * Получение производства ресурсов этим модулем
     * @param modules - Список всех модулей
     * @param production - Список для возвращения производства ресурсов
     */
    void getProduction(List<Module> modules, List<Long> production);

    /**
     * Получение потребления ресурсов этим модулем
     * @param modules     - Список всех модулей
     * @param consumption - Список для возвращения потребления ресурсов
     */
    void getConsumption(List<Module> modules, List<Long> consumption);

    /**
     * Проверяет, перекрывается ли этот компонент с другим по следующим параметрам:
     * @param x Координата X модуля
     * @param y Координата Y модуля
     * @param w Ширина модуля
     * @param h Высота модуля
     * @return true, если есть перекрытие
     */
    boolean cross(int x, int y, int w, int h);

    /**
     * Метод необходим для расчета перекрытия солнечной электростанции этим модулем
     * @return Радиус модуля, 0 если жилой
     */
    int getRadius();

    /**
     * Проверяет, достаточно ли людей для управления этим модулем, считая по порядку (id).
     * @param modules Список всех модулей
     * @param id Идентификатор текущего проверяемого модуля
     * @return true, если персонала достаточно
     */
    default boolean enoughPeople(List<Module> modules, long id){
        modules.sort(Module::compareTo);
        int countPeople = 0;
        int needPeople = 0;
        boolean cur = false;
        for(Module module : modules){
            if(module.getModule_type() == TypeModule.LIVE_MODULE_Y.ordinal() ||
                    module.getModule_type() == TypeModule.LIVE_MODULE_X.ordinal()){
                countPeople += 8;
                continue;
            }
            if(!cur){
                needPeople += TypeModule.values()[module.getModule_type()].getPeople();
            }
            if(module.getId() == id) cur = true;
        }
        return countPeople >= needPeople;
    }

    /**
     * Находит индекс космодрома в списке модулей
     */
    default Optional<Integer> findCosmodromeIndex(List<Module> modules) {
        for (int i = 0; i < modules.size(); i++) {
            if (modules.get(i).getModule_type() == TypeModule.COSMODROME.ordinal()) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    /**
     * Проверяет наличие АМ в данной зоне
     */
    default boolean checkAdmin(List<Module> modules, int idZone) {
        for (Module module : modules) {
            if (Objects.equals(module.getId_zone(), idZone)) {
                if (module.getModule_type() == TypeModule.ADMINISTRATIVE_MODULE.ordinal() ||
                        module.getModule_type() == TypeModule.LIVE_ADMINISTRATIVE_MODULE.ordinal()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Проверяет коллизии с другими модулями
     */
    default boolean hasCollisionsWithOtherModules(List<Module> modules, long id, int idZone, int x, int y, int w, int h) {
        return modules.stream()
                .filter(m -> Objects.equals(m.getId_zone(), idZone))
                .filter(m -> !Objects.equals(m.getId(), id)) // Исключаем текущий модуль
                .anyMatch(m -> {
                    Component c = TypeModule.values()[m.getModule_type()].createModule(m);

                    // Проверка пересечения с текущим модулем
                    if (c.cross(x, y, w, h)) {
                        return true;
                    }

                    // Специальная проверка для космодрома
                    if (m.getModule_type() == TypeModule.COSMODROME.ordinal()) {
                        return cross(
                                m.getX() - DANGER_ZONE,
                                m.getY() - DANGER_ZONE,
                                COSMODROME_W + 2 * DANGER_ZONE,
                                COSMODROME_H + 2 * DANGER_ZONE
                        );
                    }

                    return false;
                });
    }

    /**
     * Проверяет связь жилого модуля с другими модулями
     */
    default boolean hasConnectWithOtherModules(List<Module> modules, long id, int idZone, int x, int y, int w, int h) {
        return modules.stream()
                .filter(m -> Objects.equals(m.getId_zone(), idZone))
                .filter(m -> !Objects.equals(m.getId(), id)) // Исключаем текущий модуль
                .anyMatch(m -> {
                    if(!TypeModule.values()[m.getModule_type()].isLive()) return false;

                    Component c = TypeModule.values()[m.getModule_type()].createModule(m);

                    // Проверка пересечения с текущим модулем
                    return c.cross(x + 1, y, w, h) || c.cross(x, y + 1, w, h) ||
                            c.cross(x - 1, y, w, h) || c.cross(x, y - 1, w, h);
                });
    }

    /**
     * Проверяет если связь между областями
     * @param z1 Область 1
     * @param z2 Область 2
     * @param links Список связей
     * @return Наличие связи
     */
    default boolean hasLink(int z1, int z2, List<Link> links){
        if(z1 == z2) return true;

        UnionFind wire = new UnionFind(Zones.getLength());
        UnionFind road = new UnionFind(Zones.getLength());
        for (Link link: links){
            if(link.getPrimaryKey().getType() == 1) {
                road.union(link.getPrimaryKey().getId_zone1(), link.getPrimaryKey().getId_zone2());
            }
            else {
                wire.union(link.getPrimaryKey().getId_zone1(), link.getPrimaryKey().getId_zone2());
            }
        }

        return wire.find(z1) == wire.find(z2) && road.find(z1) == road.find(z2);
    }

    /**
     * Класс для проверки связности областей с использованием структуры данных "Система непересекающихся множеств" (Disjoint Set Union).
     * Поддерживает две основные операции: поиск корня элемента и объединение двух множеств.
     */
    class UnionFind {
        private final int[] parent; // Массив для хранения родительских элементов (предков)
        private final int[] rank;   // Массив для хранения ранга дерева (используется для оптимизации объединения)

        /**
         * Конструктор класса. Инициализирует структуру для работы с элементами от 1 до size.
         *
         * @param size Максимальный номер элемента (размер структуры)
         */
        public UnionFind(int size) {
            parent = new int[size + 1]; // +1 чтобы использовать индексы 1..size
            rank = new int[size + 1];
            for (int i = 0; i <= size; i++) {
                parent[i] = i; // Изначально каждый элемент является своим родителем
            }
        }

        /**
         * Находит корневой элемент для заданного элемента x с применением сжатия пути.
         *
         * @param x Элемент, для которого ищется корень
         * @return Корневой элемент множества, содержащего x
         */
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Рекурсивное сжатие пути
            }
            return parent[x];
        }

        /**
         * Объединяет два множества, содержащие элементы x и y.
         * Использует объединение по рангу для оптимизации производительности.
         *
         * @param x Первый элемент
         * @param y Второй элемент
         */
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) { // Объединяем только если элементы в разных множествах
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX; // Корень с меньшим рангом подчиняется корню с большим рангом
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX; // При равных рангах выбирается произвольный корень
                    rank[rootX]++;         // и его ранг увеличивается
                }
            }
        }
    }
}
