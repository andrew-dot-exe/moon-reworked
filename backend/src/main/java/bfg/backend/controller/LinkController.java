package bfg.backend.controller;


import bfg.backend.repository.link.Link;
import bfg.backend.service.LinkService;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления связями между областями.
 * Предоставляет API для создания связей.
 */
@RestController
@RequestMapping(path = "link")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    /**
     * Создает новую связь между областями.
     *
     * @param link DTO с данными для создания связи
     * @return Количесво затраченных материалов для создания
     * @see Link
     */
    @PostMapping
    public Long create(@RequestBody Link link){
        return linkService.create(link);
    }
}
