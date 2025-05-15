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

import static bfg.backend.service.logic.Constants.*;
import static bfg.backend.service.logic.Constants.DANGER_ZONE;

public class ManufacturingEnterprise extends Module implements Component {
    private final static int h = 1;
    private final static int w = 1;
    private final static double MAX_ANGLE = 10;

    public ManufacturingEnterprise(Module module) {
        super(module.getId(), module.getId_user(), module.getId_zone(),
                module.getModule_type(), module.getX(), module.getY());
    }

    public ManufacturingEnterprise(Long id, Long id_user, Integer id_zone, Integer module_type, Integer x, Integer y) {
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
        if(!enoughPeople(modules, getId())) return null;
        if(!checkAdmin(modules, getId_zone())){
            return null;
        }
        if (hasCollisionsWithOtherModules(modules, getId(), getId_zone(), getX(), getY(), w, h)) return null;

        for (Module module : modules){
            if(Objects.equals(module.getId_zone(), getId_zone())){
                if(module.getModule_type() == TypeModule.MINE_BASE.ordinal()){
                    return Math.max(0, getEffect(resources));
                }
            }
        }
        return null;
    }

    private int getEffect(List<Resource> resources){
        int o2 = (int) (100 - resources.get(TypeResources.O2.ordinal()).getProduction() / resources.get(TypeResources.O2.ordinal()).getConsumption() * 100);
        int h20 = (int) (100 - resources.get(TypeResources.H2O.ordinal()).getProduction() / resources.get(TypeResources.H2O.ordinal()).getConsumption() * 100);
        int mat = (int) (100 - resources.get(TypeResources.MATERIAL.ordinal()).getProduction() / resources.get(TypeResources.MATERIAL.ordinal()).getConsumption() * 100);

        return (o2 + 2 * h20 + mat) / 4;
    }

    @Override
    public void getProduction(List<Module> modules, List<Long> production) {
        production.set(TypeResources.H2O.ordinal(), production.get(TypeResources.H2O.ordinal()) + 500L);
        production.set(TypeResources.O2.ordinal(), production.get(TypeResources.O2.ordinal()) + 5200L);
        production.set(TypeResources.MATERIAL.ordinal(), production.get(TypeResources.MATERIAL.ordinal()) + 12300L);
    }

    @Override
    public void getConsumption(List<Module> modules, List<Long> consumption) {
        consumption.set(TypeResources.WT.ordinal(), consumption.get(TypeResources.WT.ordinal()) +  64740L);
    }

    @Override
    public boolean cross(int x, int y, int w, int h) {
        return (x >= getX() && x <= getX() + ManufacturingEnterprise.w && y >= getY() && y <= getY() + ManufacturingEnterprise.h) ||
                (getX() >= x && getX() <= x + w && getY() >= y && getY() <= y + h);
    }

    @Override
    public int getRadius() {
        return (h + w) / 4;
    }
}
