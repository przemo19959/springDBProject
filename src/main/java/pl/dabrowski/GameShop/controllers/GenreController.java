package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dabrowski.GameShop.assemblers.GenreAssembler;
import pl.dabrowski.GameShop.repositories.GenreRepository;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(GenreController.BASE_URL)
public class GenreController {
    final static String BASE_URL = "/genres";
    private final GenreRepository genreRepository;
    private final GenreAssembler genreAssembler;

    @Autowired
    public GenreController(GenreRepository myRepository, GenreAssembler genreAssembler) {
        this.genreRepository = myRepository;
        this.genreAssembler = genreAssembler;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(genreAssembler.toCollectionModel(genreRepository.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        return ResponseEntity.ok(genreAssembler.toModel(genreRepository.findById(id).orElseThrow(NoSuchElementException::new)));
    }
//
////    @GetMapping("/{id}")
////    @ResponseBody
////    public ResponseEntity<EntityModel<Genre>> findById(@PathVariable int id){
////        Genre genre=genreRepository.findById(id).get();
////        return ResponseEntity.ok(EntityModel.of(genre,
////                linkTo(methodOn(GenreController.class).findById(id)).withSelfRel(),
////                linkTo(methodOn(GenreController.class).toString(id)).withRel("toString")));
////    }
////
////    @GetMapping("/{id}/toString")
////    @ResponseBody
////    public ResponseEntity<String> toString(@PathVariable int id){
////        return ResponseEntity.ok(genreRepository.toString(genreRepository.findById(id).get()));
////    }
//
////    @GetMapping("/search")
////    @ResponseBody
////    public ResponseEntity<?> search() {
////        return ResponseEntity.ok(EntityModel.of("",
////                linkTo(methodOn(GenreController.class).findKeys()).withRel("keys")));
////    }
////
////    @GetMapping("/search/findKeys")
////    @ResponseBody
////    public ResponseEntity<Iterable<KeyDTO>> findKeys() {
////        return ResponseEntity.ok(genreRepository.findKeys(Genre.class));
////    }
}
