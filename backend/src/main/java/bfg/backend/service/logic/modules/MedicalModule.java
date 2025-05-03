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

public class MedicalModule extends Module implements Component {
    private final static int h = 1;
    private final static int w = 1;
    private final static double MAX_ANGLE = 10;

    public MedicalModule(Module module) {
        super(module.getId(), module.getId_user(), module.getId_zone(),
                module.getModule_type(), module.getX(), module.getY());
    }

    public MedicalModule(Long id, Long id_user, Integer id_zone, Integer module_type, Integer x, Integer y) {
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
        if(hasCollisionsWithOtherModules(modules, getId(), getId_zone(), getX(), getY(), w, h)) return null;
        if(!hasConnectWithOtherModules(modules, getId(), getId_zone(), getX(), getY(), w, h)){
            return null;
        }
        return Math.min(100, getEffect(modules));
    }

    private int getEffect(List<Module> modules){
        int[] count = {0, 0}; // медицинских, жилых
        modules.stream()
                .filter(e -> Objects.equals(e.getId_zone(), getId_zone()) &&
                        !Objects.equals(e.getId(), getId()))
                .forEach(e -> {
                    if(Objects.equals(e.getModule_type(),getModule_type())) count[0] += 3;
                    else if(Objects.equals(e.getModule_type(), TypeModule.LIVE_MODULE_Y.ordinal()) ||
                            Objects.equals(e.getModule_type(), TypeModule.LIVE_MODULE_X.ordinal())){
                        count[1]++;
                    }
                });
        return 100 - (count[0] - count[1]) / count[0] * 100;
    }

    @Override
    public void getProduction(List<Module> modules, List<Long> production) {

    }

    @Override
    public void getConsumption(List<Module> modules, List<Long> consumption) {
        consumption.set(TypeResources.WT.ordinal(), consumption.get(TypeResources.WT.ordinal()) + 7000L);
    }

    @Override
    public boolean cross(int x, int y, int w, int h) {
        return (x >= getX() && x <= getX() + MedicalModule.w && y >= getY() && y <= getY() + MedicalModule.h) ||
                (getX() >= x && getX() <= x + w && getY() >= y && getY() <= y + h);
    }

    @Override
    public int getRadius() {
        return 0;
    }
}
