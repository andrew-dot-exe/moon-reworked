package bfg.backend.service.logic.modules;

import bfg.backend.repository.link.Link;
import bfg.backend.repository.module.Module;
import bfg.backend.repository.resource.Resource;
import bfg.backend.service.logic.Component;
import bfg.backend.service.logic.TypeModule;
import bfg.backend.service.logic.TypeResources;
import bfg.backend.service.logic.zones.Zones;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;

import static bfg.backend.service.logic.Constants.SIZE_CELL;

public class SolarPowerPlant extends Module implements Component {
    private final static int h = 1;
    private final static int w = 1;
    private final static double MAX_ANGLE = 10;

    public SolarPowerPlant(Module module) {
        super(module.getId(), module.getId_user(), module.getId_zone(),
                module.getModule_type(), module.getX(), module.getY());
    }

    public SolarPowerPlant(Long id, Long id_user, Integer id_zone, Integer module_type, Integer x, Integer y) {
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
        if(!checkAdmin(modules, getId_zone())) return null;
        ShadowCalculator shadowCalculator = new ShadowCalculator();
        SolarObject solarObject = new SolarObject(getX() * SIZE_CELL, getY() * SIZE_CELL, getRadius());
        for (Module module : modules){

            if(Objects.equals(module.getId_zone(), getId_zone())){
                if(Objects.equals(module.getId(), getId())) continue;
                Component c = TypeModule.values()[module.getModule_type()].createModule(module);
                if(c.cross(getX(), getY(), w, h)){
                    return null;
                }
                shadowCalculator.addShadowFromModule(solarObject, module, c.getRadius());
            }
        }
        return (int) shadowCalculator.calculateTotalEfficiency(Zones.getZones().get(getId_zone()).getIllumination());
    }

    @Override
    public void getProduction(List<Module> modules, List<Long> production) {
        production.set(TypeResources.WT.ordinal(), production.get(TypeResources.WT.ordinal()) + 162500L * getRationality(modules, null, null));
    }

    @Override
    public void getConsumption(List<Module> modules, List<Long> consumption) {
        consumption.set(TypeResources.WT.ordinal(), consumption.get(TypeResources.WT.ordinal()) + 1200L);
    }

    @Override
    public boolean cross(int x, int y, int w, int h) {
        return (x >= getX() && x <= getX() + SolarPowerPlant.w && y >= getY() && y <= getY() + SolarPowerPlant.h) ||
                (getX() >= x && getX() <= x + w && getY() >= y && getY() <= y + h);
    }

    @Override
    public int getRadius() {
        return (h + w) / 4;
    }

    /**
     */
    static class SolarObject {
        private double x;
        private double y;
        private double radius;

        public SolarObject(double x, double y, double radius){
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getRadius() {
            return radius;
        }

        public void setRadius(double radius) {
            this.radius = radius;
        }

        /**
         * Вычисляет расстояние до другого объекта
         */
        public double distanceTo(SolarObject other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        /**
         * Вычисляет угол (в градусах) от текущего объекта к другому
         * Возвращает значение от 0 до 360
         */
        public double angleTo(SolarObject other) {
            double dx = other.x - this.x;
            double dy = other.y - this.y;
            return (int) (Math.atan2(dy, dx) * 180 / Math.PI + 360) % 360;
        }
    }

    static class ShadowCalculator {
        private static final int DEGREES = 360;
        private final BitSet illumination;

        public ShadowCalculator() {
            illumination = new BitSet(DEGREES);
            illumination.set(0, DEGREES); // Изначально все углы освещены
        }

        /**
         * Добавляет тень от объекта
         * @param station - солнечная станция
         * @param obstacle - препятствие
         */
        public void addShadow(SolarObject station, SolarObject obstacle) {
            double distance = station.distanceTo(obstacle);
            double angle = station.angleTo(obstacle);
            double shadowAngle = 2 * (Math.asin(obstacle.getRadius() / distance) * 180 / Math.PI + 360) % 360;

            markShadow(angle, shadowAngle);
        }

        /**
         * Помечает углы как затененные
         * @param centerAngle - центральный угол тени
         * @param shadowWidth - ширина тени в градусах
         */
        private void markShadow(double centerAngle, double shadowWidth) {
            double halfShadow = shadowWidth / 2 + DEGREES;
            int start = (int) Math.floor(centerAngle - halfShadow) % DEGREES;
            int end = (int) Math.ceil(centerAngle + halfShadow) % DEGREES;

            if (start <= end) {
                illumination.clear(start, end + 1);
            } else {
                illumination.clear(start, DEGREES);
                illumination.clear(0, end + 1);
            }
        }

        /**
         * Вычисляет относительную эффективность
         * @return эффективность в процентах
         */
        public double calculateEfficiency() {
            return (illumination.cardinality() * 100.0) / DEGREES;
        }

        /**
         * Вычисляет итоговую эффективность станции
         * @param maxEfficiency - максимальная эффективность станции
         */
        public double calculateTotalEfficiency(double maxEfficiency) {
            return maxEfficiency * calculateEfficiency() / 100.0;
        }

        /**
         * Переиспользуемый объект для уменьшения нагрузки на GC
         */
        private static final SolarObject reusableObstacle = new SolarObject(0, 0, 0);

        /**
         * Оптимизированный метод добавления тени для модуля
         */
        public void addShadowFromModule(SolarObject station, Module module, double r) {
            reusableObstacle.x = module.getX() * SIZE_CELL;
            reusableObstacle.y = module.getY() * SIZE_CELL;
            reusableObstacle.radius = r;
            addShadow(station, reusableObstacle);
        }
    }
}
