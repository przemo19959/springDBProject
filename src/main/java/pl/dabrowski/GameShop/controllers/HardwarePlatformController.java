package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.dabrowski.GameShop.entities.AgeCategory;
import pl.dabrowski.GameShop.entities.HardwarePlatform;
import pl.dabrowski.GameShop.repositories.HardwarePlatformRepository;
import pl.dabrowski.GameShop.repositories.custom.KeyDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RepositoryRestResource
@RequestMapping(HardwarePlatformController.BASE_URL)
public class HardwarePlatformController {
    final static String BASE_URL = "/hardwarePlatforms";
    private final HardwarePlatformRepository hardwarePlatformRepository;

    @Autowired
    public HardwarePlatformController(HardwarePlatformRepository hardwarePlatformRepository) {
        this.hardwarePlatformRepository = hardwarePlatformRepository;
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<?> search() {
        return ResponseEntity.ok(EntityModel.of("",
                linkTo(methodOn(HardwarePlatformController.class).findKeys()).withRel("keys")));
    }

    @GetMapping("/search/findKeys")
    @ResponseBody
    public ResponseEntity<Iterable<KeyDTO>> findKeys() {
        return ResponseEntity.ok(hardwarePlatformRepository.findKeys(HardwarePlatform.class));
    }
}
