package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dabrowski.GameShop.assemblers.GameplayModeAssembler;
import pl.dabrowski.GameShop.entities.GameplayMode;
import pl.dabrowski.GameShop.repositories.GameplayModeRepository;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(GameplayModeController.BASE_URL)
public class GameplayModeController {
	final static String BASE_URL = "/gameplayModes";
	private final GameplayModeRepository gameplayModeRepository;
	private final GameplayModeAssembler gameplayModeAssembler;

	@Autowired
	public GameplayModeController(GameplayModeRepository myRepository, GameplayModeAssembler gameplayModeAssembler) {
		this.gameplayModeRepository = myRepository;
		this.gameplayModeAssembler = gameplayModeAssembler;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<GameplayMode>>> findAll() {
		return ResponseEntity.ok(gameplayModeAssembler.toCollectionModel(gameplayModeRepository.findAll()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<GameplayMode>> findById(@PathVariable int id) {
		return ResponseEntity.ok(gameplayModeAssembler.toModel(gameplayModeRepository.findById(id)//
				.orElseThrow(NoSuchElementException::new)));
	}
}
