package bfg.backend.service.logic;

import bfg.backend.repository.link.Link;
import bfg.backend.repository.module.Module;
import bfg.backend.repository.resource.Resource;

import java.util.List;

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
