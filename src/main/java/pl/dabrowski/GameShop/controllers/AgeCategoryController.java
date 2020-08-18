package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dabrowski.GameShop.assemblers.AgeCategoryAssembler;
import pl.dabrowski.GameShop.repositories.AgeCategoryRepository;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(AgeCategoryController.BASE_URL)
public class AgeCategoryController {
    final static String BASE_URL = "/ageCategories";
    private final AgeCategoryRepository ageCategoryRepository;
    private final AgeCategoryAssembler ageCategoryAssembler;

    @Autowired
    public AgeCategoryController(AgeCategoryRepository myRepository, AgeCategoryAssembler ageCategoryAssembler) {
        this.ageCategoryRepository = myRepository;
        this.ageCategoryAssembler = ageCategoryAssembler;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(ageCategoryAssembler.toCollectionModel(ageCategoryRepository.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        return ResponseEntity.ok(ageCategoryAssembler.toModel(ageCategoryRepository.findById(id).orElseThrow(NoSuchElementException::new)));
    }

//
//    @GetMapping("/search")
//    @ResponseBody
//    public ResponseEntity<?> search() {
//        return ResponseEntity.ok(EntityModel.of("",
//                linkTo(methodOn(AgeCategoryController.class).findKeys()).withRel("keys")));
//    }
//
//    @GetMapping("/search/findKeys")
//    @ResponseBody
//    public ResponseEntity<Iterable<KeyDTO>> findKeys() {
//        return ResponseEntity.ok(ageCategoryRepository.findKeys(AgeCategory.class));
//    }
}
