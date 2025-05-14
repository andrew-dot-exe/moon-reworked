package bfg.backend.service.logic.modules;

import bfg.backend.repository.link.Link;
import bfg.backend.repository.module.Module;
import bfg.backend.repository.resource.Resource;
import bfg.backend.service.logic.Component;
import bfg.backend.service.logic.TypeModule;
import bfg.backend.service.logic.TypeResources;
import bfg.backend.service.logic.zones.Zones;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static bfg.backend.service.logic.Constants.*;
import static bfg.backend.service.logic.Constants.DANGER_ZONE;

public class AdministrativeModule extends Module implements Component {

    private final static int h = 2;
    private final static int w = 2;
    private final static double MAX_ANGLE = 10;


    public AdministrativeModule(Module module) {
        super(module.getId(), module.getId_user(), module.getId_zone(),
                module.getModule_type(), module.getX(), module.getY());
    }

    public AdministrativeModule(Long id, Long id_user, Integer id_zone, Integer module_type, Integer x, Integer y) {
        super(id, id_user, id_zone, module_type, x, y);
    }

    @Override
    public Integer getRelief() {
        int x = getX();
        int y = getY();
        double maxAngle = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                try {
                    maxAngle = Math.max(maxAngle, Zones.getZones().get(getId_zone()).getCells()[y + i][x + j].getAngle());
                }catch (ArrayIndexOutOfBoundsException e){
                    return null;
                }
            }
        }
        int res = (int) ((MAX_ANGLE - maxAngle) * 10);
        if(res <= 0) return null;
        return res;
    }

    @Override
    public Integer getRationality(List<Module> modules, List<Link> links, List<Resource> resources) {
        if (hasConflictingModules(modules)) {
            return null;
        }

        Optional<Integer> cosmodromeIndex = findCosmodromeIndex(modules);
        if (cosmodromeIndex.isEmpty()) {
            return null;
        }
        int cos = cosmodromeIndex.get();

        if (hasCollisionsWithOtherModules(modules, getId(), getId_zone(), getX(), getY(), w, h)) {
            return null;
        }

        if(hasLink(getId_zone(), modules.get(cos).getId_zone(), links)) return 100;
        return null;
    }

    /**
     * Проверяет наличие конфликтующих модулей в той же зоне
     */
    private boolean hasConflictingModules(List<Module> modules) {
        return modules.stream()
                .filter(m -> Objects.equals(m.getId_zone(), getId_zone()))
                .anyMatch(m -> Objects.equals(m.getModule_type(), getModule_type()) ||
                        m.getModule_type() == TypeModule.LIVE_ADMINISTRATIVE_MODULE.ordinal());
    }

    @Override
    public void getProduction(List<Module> modules, List<Long> production) {}

    @Override
    public void getConsumption(List<Module> modules, List<Long> consumption) {
        consumption.set(TypeResources.WT.ordinal(), consumption.get(TypeResources.WT.ordinal()) + 123000L);
    }

    @Override
    public boolean cross(int x, int y, int w, int h) {
        return (x >= getX() && x <= getX() + AdministrativeModule.w && y >= getY() && y <= getY() + AdministrativeModule.h) ||
                (getX() >= x && getX() <= x + w && getY() >= y && getY() <= y + h);
    }

    @Override
    public int getRadius() {
        return (h + w) / 4;
    }
}
