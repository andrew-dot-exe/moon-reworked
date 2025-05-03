package bfg.backend.controller;

import bfg.backend.dto.responce.allUserInfo.Resource;
import bfg.backend.service.ProductionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для получения ресурсов пользователя.
 */
@RestController
@RequestMapping(path = "product")
public class ProductionController {

    private final ProductionService productionService;

    public ProductionController(ProductionService productionService) {
        this.productionService = productionService;
    }

    /**
     * Возвращает данные о ресурсах пользователя
     * @return Список ресурсов с их количеством, производством и типом
     */
    @GetMapping()
    public List<Resource> resources() {
        return productionService.getResources();
    }
}
