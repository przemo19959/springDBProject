package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dabrowski.GameShop.assemblers.ConsoleGameAssembler;
import pl.dabrowski.GameShop.repositories.ConsoleGameRepository;

import java.util.NoSuchElementException;


@RepositoryRestController
@RequestMapping(ConsoleGameController.BASE_URL)
public class ConsoleGameController {
    final static String BASE_URL = "/consoleGames";

    private final ConsoleGameRepository consoleGameRepository;
    private final ConsoleGameAssembler consoleGameAssembler;

    @Autowired
    public ConsoleGameController(ConsoleGameRepository myRepository, ConsoleGameAssembler consoleGameAssembler) {
        this.consoleGameRepository = myRepository;
        this.consoleGameAssembler = consoleGameAssembler;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(consoleGameAssembler.toCollectionModel(consoleGameRepository.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        return ResponseEntity.ok(consoleGameAssembler.toModel(consoleGameRepository.findById(id).orElseThrow(NoSuchElementException::new)));
    }

//    @GetMapping("/search")
//    @ResponseBody
//    public ResponseEntity<?> search() {
//        return ResponseEntity.ok(EntityModel.of("",
//                linkTo(methodOn(ConsoleGameController.class).findKeys()).withRel("keys")));
//    }
//
//    @GetMapping("/search/findKeys")
//    @ResponseBody
//    public ResponseEntity<Iterable<KeyDTO>> findKeys() {
//        return ResponseEntity.ok(consoleGameRepository.findKeys(ConsoleGame.class));
//    }
}
