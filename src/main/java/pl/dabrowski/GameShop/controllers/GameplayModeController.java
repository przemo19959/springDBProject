package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dabrowski.GameShop.assemblers.GameplayModeAssembler;
import pl.dabrowski.GameShop.entities.GameplayMode;
import pl.dabrowski.GameShop.repositories.GameplayModeRepository;

import static pl.dabrowski.GameShop.controllers.RootController.DEFAULT_EXAMPLE_VALUE;

import java.util.NoSuchElementException;

import javax.validation.Valid;

@RestController
@RequestMapping(GameplayModeController.BASE_URL)
public class GameplayModeController {
	final static String BASE_URL = "/gameplayModes";
	private final GameplayModeRepository gameplayModeRepository;
	private final GameplayModeAssembler gameplayModeAssembler;
	
	public static final GameplayMode EXAMPLE;
	static {
		EXAMPLE=new GameplayMode();
		EXAMPLE.setName(DEFAULT_EXAMPLE_VALUE);
	}

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
	
	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<GameplayMode>> update(@Valid @RequestBody GameplayMode newGameplayMode,
			@PathVariable int id) {
		return ResponseEntity.ok(gameplayModeRepository.findById(id).map(gameplayMode -> {
			gameplayMode.setName(newGameplayMode.getName());
			return gameplayModeAssembler.toModel(gameplayModeRepository.save(gameplayMode));
		}).orElseThrow(NoSuchElementException::new));
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<GameplayMode>> save(@Valid @RequestBody GameplayMode newGameplayMode){
		return new ResponseEntity<>(gameplayModeAssembler.toModel(gameplayModeRepository.save(newGameplayMode)), HttpStatus.CREATED); 
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		gameplayModeRepository.delete(gameplayModeRepository.findById(id).orElseThrow(NoSuchElementException::new));
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/example")
	public ResponseEntity<EntityModel<GameplayMode>> example(){
		return ResponseEntity.ok(gameplayModeAssembler.toModel(EXAMPLE));
	}
}
