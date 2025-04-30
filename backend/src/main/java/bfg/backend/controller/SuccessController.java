package bfg.backend.controller;


import bfg.backend.dto.responce.successful.Successful;
import bfg.backend.service.SuccessfulService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "success")
public class SuccessController {

    private final SuccessfulService successfulService;

    public SuccessController(SuccessfulService successfulService) {
        this.successfulService = successfulService;
    }

    @GetMapping()
    public Successful successful(){
        return successfulService.getSuccessful();
    }
}
