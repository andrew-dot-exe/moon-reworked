package bfg.backend.service.logic;

import bfg.backend.dto.responce.moduleType.ModuleType;
import bfg.backend.repository.module.Module;
import bfg.backend.service.logic.modules.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Перечисление всех типов модулей.
 * Определяет свойства и поведение каждого типа модуля, включая:
 * - Название
 * - Необходимый персонал
 * - Стоимость строительства
 * - Является ли жилым
 * - Конструктор соответствующего компонента
 */
public enum TypeModule {
    LIVE_MODULE_X("Жилой модуль", 0, 9500, true, LiveModuleX::new),
    LIVE_MODULE_Y("Жилой модуль", 0, 9500, true, LiveModuleY::new),
    LIVE_ADMINISTRATIVE_MODULE("Административный модуль", 2, 16000, true, LiveAdministrativeModule::new),
    SPORT_MODULE("Спортивный модуль", 0, 5000, true, SportModule::new),
    MEDICAL_MODULE("Медицинский модуль", 1, 5000, true, MedicalModule::new),
    PLANTATION("Плантация", 3, 42000, true, Plantation::new),
    RESEARCH_MODULE_PLANTATION("Исследовательский модуль", 1, 5000, true, ResearchModulePlantation::new),
    RESEARCH_MODULE_MINE("Исследовательский модуль", 1, 5000, true, ResearchModuleMine::new),
    RESEARCH_MODULE_TELESCOPE("Исследовательский модуль", 1, 5000, true, ResearchModuleTelescope::new),
    RESEARCH_MODULE_TERRITORY("Исследовательский модуль", 1, 5000, true, ResearchModuleTerritory::new),
    HALLWAY("Коридор", 0, 3500, true, Hallway::new),
    ADMINISTRATIVE_MODULE("Административный модуль", 2, 16000, false, AdministrativeModule::new),
    SOLAR_POWER_PLANT("Солнечная электростанция", 0, 1800, false, SolarPowerPlant::new),
    REPAIR_MODULE("Ремонтный модуль", 2, 15000, false, RepairModule::new),
    COSMODROME("Космодром", 2, 900, false, Cosmodrome::new),
    COMMUNICATION_TOWER("Вышка связи", 0, 1000, false, CommunicationTower::new),
    LANDFILL("Мусорный полигон", 1, 15000, false, Landfill::new),
    LANDFILL_BIO("Мусорный полигон", 1, 15000, false, LandfillBio::new),
    MANUFACTURING_ENTERPRISE("Производственное предприятие", 1, 4200, false, ManufacturingEnterprise::new),
    MANUFACTURING_ENTERPRISE_FUEL("Производственное предприятие", 1, 4200, false, ManufacturingEnterpriseFuel::new),
    ASTRONOMICAL_SITE("Астрономическая площадка", 0, 30000, false, AstronomicalSite::new),
    MINE_BASE("База шахты", 1, 20000, false, MineBase::new),
    WAREHOUSE_FOOD("Склад", 0, 14000, false, WarehouseFood::new),
    WAREHOUSE_GASES("Склад", 0, 14000, false, WarehouseGases::new),
    WAREHOUSE_FUEL("Склад", 0, 14000, false, WarehouseFuel::new),
    WAREHOUSE_MATERIAL("Склад", 0, 14000, false, WarehouseMaterial::new);

    private final String name;
    private final int cost;
    private final int people;
    private final boolean live;

    private interface Constructor{Component create(Module module);};
    private final Constructor constructor;

    TypeModule(String name, int people, int cost, boolean live, Constructor c){
        this.name = name;
        this.live = live;
        this.people = people;
        this.cost = cost;
        constructor = c;
    }

    public Component createModule(Module module){
        return constructor.create(module);
    }

    public Component createModule(Long idUser, Integer idZone, Integer x, Integer y){
        return constructor.create(new Module(-1L, idUser,idZone, this.ordinal(), x, y));
    }

    public String getName(){
        return name;
    }

    public int getCost() {
        return cost;
    }

    public boolean isLive() {
        return live;
    }

    public int getPeople() {
        return people;
    }


    public static List<ModuleType> getTypes(){
        int l = TypeModule.values().length;
        List<ModuleType> res = new ArrayList<>(l);
        for (int i = 0; i < l; i++) {
            TypeModule t = TypeModule.values()[i];
            res.add(new ModuleType(i, t.getName(), t.getPeople(), t.getCost(), t.isLive()));
        }
        return res;
    }
}
