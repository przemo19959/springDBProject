package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.dabrowski.GameShop.assemblers.ConsoleGameAssembler;
import pl.dabrowski.GameShop.entities.ConsoleGame;
import pl.dabrowski.GameShop.repositories.ConsoleGameRepository;

import java.util.NoSuchElementException;

@RestController
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
    public ResponseEntity<CollectionModel<EntityModel<ConsoleGame>>> findAll() {
        return ResponseEntity.ok(consoleGameAssembler.toCollectionModel(consoleGameRepository.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ConsoleGame>> findById(@PathVariable int id) {
        return ResponseEntity.ok(consoleGameAssembler.toModel(consoleGameRepository.findById(id)//
        		.orElseThrow(NoSuchElementException::new)));
    }
}
