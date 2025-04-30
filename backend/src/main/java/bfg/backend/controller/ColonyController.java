package bfg.backend.controller;

import bfg.backend.dto.responce.allUserInfo.AllUserInfo;
import bfg.backend.service.ColonyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "colony")
public class ColonyController {

    private final ColonyService colonyService;

    public ColonyController(ColonyService colonyService) {
        this.colonyService = colonyService;
    }

    @DeleteMapping()
    public void delete(){
        colonyService.delete();
    }

    @GetMapping
    public void create(){
        colonyService.create();
    }

}
