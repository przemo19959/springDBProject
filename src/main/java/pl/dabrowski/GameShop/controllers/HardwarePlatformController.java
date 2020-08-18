package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dabrowski.GameShop.assemblers.HardwarePlatformAssembler;
import pl.dabrowski.GameShop.repositories.HardwarePlatformRepository;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(HardwarePlatformController.BASE_URL)
public class HardwarePlatformController {
    final static String BASE_URL = "/hardwarePlatforms";
    private final HardwarePlatformRepository hardwarePlatformRepository;
    private final HardwarePlatformAssembler hardwarePlatformAssembler;

    @Autowired
    public HardwarePlatformController(HardwarePlatformRepository hardwarePlatformRepository, HardwarePlatformAssembler hardwarePlatformAssembler) {
        this.hardwarePlatformRepository = hardwarePlatformRepository;
        this.hardwarePlatformAssembler = hardwarePlatformAssembler;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(hardwarePlatformAssembler.toCollectionModel(hardwarePlatformRepository.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        return ResponseEntity.ok(hardwarePlatformAssembler.toModel(hardwarePlatformRepository.findById(id).orElseThrow(NoSuchElementException::new)));
    }
//
//    @GetMapping("/search")
//    @ResponseBody
//    public ResponseEntity<?> search() {
//        return ResponseEntity.ok(EntityModel.of("",
//                linkTo(methodOn(HardwarePlatformController.class).findKeys()).withRel("keys")));
//    }
//
//    @GetMapping("/search/findKeys")
//    @ResponseBody
//    public ResponseEntity<Iterable<KeyDTO>> findKeys() {
//        return ResponseEntity.ok(hardwarePlatformRepository.findKeys(HardwarePlatform.class));
//    }
}
