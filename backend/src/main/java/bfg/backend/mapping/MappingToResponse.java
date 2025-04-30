package bfg.backend.mapping;

import bfg.backend.dto.responce.allUserInfo.*;
import bfg.backend.dto.responce.area.CellsOfZone;
import bfg.backend.dto.responce.area.Zone;
import bfg.backend.repository.link.Link;
import bfg.backend.repository.module.Module;
import bfg.backend.repository.user.User;
import bfg.backend.repository.resource.Resource;
import bfg.backend.service.logic.zones.Area;
import bfg.backend.service.logic.zones.Zones;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MappingToResponse {

    public static AllUserInfo mapToAllUserInfo(User user, List<Module> modules, List<Link> links, List<Resource> resources){
        List<bfg.backend.dto.responce.allUserInfo.Module> resMod = new LinkedList<>();
        List<bfg.backend.dto.responce.allUserInfo.Link> resLink = new LinkedList<>();
        List<bfg.backend.dto.responce.allUserInfo.Resource> resResource = new LinkedList<>();

        for(Module module : modules){
            resMod.add(new bfg.backend.dto.responce.allUserInfo.Module(module));
        }
        for (Resource resource : resources){
            resResource.add(new bfg.backend.dto.responce.allUserInfo.Resource(resource));
        }
        for (Link link : links){
            resLink.add(new bfg.backend.dto.responce.allUserInfo.Link(link));
        }

        return new AllUserInfo(user.getName(), user.getCurrent_day(), user.getDays_before_delivery(), user.getLive(), resResource, resLink, resMod);
    }

    public static List<Zone> mapToZone(){
        int l = Zones.getLength();
        List<Zone> zones = new ArrayList<>(l);
        for (int i = 0; i < l; i++) {
            Area area = Zones.getZones().get(i);
            zones.add(new Zone(area.getName(), area.getWidthSecond(), area.getLongitudeSecond(), area.getIllumination(), area.getWays()));
        }
        return zones;
    }

    public static CellsOfZone mapToCells(int i){
        return new CellsOfZone(Zones.getZones().get(i).getCells());
    }
}
