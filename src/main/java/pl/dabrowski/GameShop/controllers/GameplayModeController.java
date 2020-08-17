package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.dabrowski.GameShop.entities.AgeCategory;
import pl.dabrowski.GameShop.entities.GameplayMode;
import pl.dabrowski.GameShop.repositories.AgeCategoryRepository;
import pl.dabrowski.GameShop.repositories.GameplayModeRepository;
import pl.dabrowski.GameShop.repositories.custom.KeyDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RepositoryRestController
@RequestMapping(GameplayModeController.BASE_URL)
public class GameplayModeController {
    final static String BASE_URL = "/gameplayModes";
    private final GameplayModeRepository gameplayModeRepository;

    @Autowired
    public GameplayModeController(GameplayModeRepository myRepository) {
        this.gameplayModeRepository = myRepository;
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<?> search() {
        return ResponseEntity.ok(EntityModel.of("",
                linkTo(methodOn(GameplayModeController.class).findKeys()).withRel("keys")));
    }

    @GetMapping("/search/findKeys")
    @ResponseBody
    public ResponseEntity<Iterable<KeyDTO>> findKeys() {
        return ResponseEntity.ok(gameplayModeRepository.findKeys(GameplayMode.class));
    }
}
