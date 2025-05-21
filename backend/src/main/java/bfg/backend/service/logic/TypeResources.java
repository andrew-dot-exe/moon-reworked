package bfg.backend.service.logic;

/**
 * Перечисление типов ресурсов в колонии с их начальными значениями.
 */
public enum TypeResources {
    H2O(600000),
    FUEL(0),
    WT(999999999),
    FOOD(450000),
    O2(0),
    CO2(0),
    GARBAGE(0),
    MATERIAL(250000000);

    private final long startCount;

    TypeResources(int startCount) {
        this.startCount = startCount;
    }

    public long getStartCount() {
        return startCount;
    }
}
