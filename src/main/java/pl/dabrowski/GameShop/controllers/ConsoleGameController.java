package pl.dabrowski.GameShop.controllers;

import static pl.dabrowski.GameShop.controllers.RootController.DEFAULT_EXAMPLE_VALUE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.dabrowski.GameShop.assemblers.ConsoleGameAssembler;
import pl.dabrowski.GameShop.entities.ConsoleGame;
import pl.dabrowski.GameShop.repositories.ConsoleGameRepository;

@RestController
@RequestMapping(ConsoleGameController.BASE_URL)
public class ConsoleGameController {
	final static String BASE_URL = "/consoleGames";

	private final ConsoleGameRepository consoleGameRepository;
	private final ConsoleGameAssembler consoleGameAssembler;
	private static final ConsoleGame EXAMPLE;
	
	static {
		EXAMPLE=new ConsoleGame();
		EXAMPLE.setTitle(DEFAULT_EXAMPLE_VALUE);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		String dateInString = "2000-01-01";
		try {
			EXAMPLE.setDateOfRelease(formatter.parse(dateInString));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		EXAMPLE.setAgeCategory(AgeCategoryController.EXAMPLE);
		EXAMPLE.setGameplayMode(GameplayModeController.EXAMPLE);
		EXAMPLE.setGenre(GenreController.EXAMPLE);
		EXAMPLE.setHardwarePlatform(HardwarePlatformController.EXAMPLE);
		EXAMPLE.setLanguage(LanguageController.EXAMPLE);
		EXAMPLE.setProducer(ProducerController.EXAMPLE);
		EXAMPLE.setPublisher(PublisherController.EXAMPLE);
	}

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
		return ResponseEntity.ok(consoleGameAssembler.toModel(consoleGameRepository.findById(id).get()));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<ConsoleGame>> update(@RequestBody ConsoleGame newConsoleGame,
			@PathVariable int id) {
		System.out.println(newConsoleGame);
		return ResponseEntity.ok(consoleGameRepository.findById(id).map(consoleGame -> {
			consoleGame.setTitle(newConsoleGame.getTitle());
			consoleGame.setDateOfRelease(newConsoleGame.getDateOfRelease());
			consoleGame.setAgeCategory(newConsoleGame.getAgeCategory());
			consoleGame.setGameplayMode(newConsoleGame.getGameplayMode());
			consoleGame.setGenre(newConsoleGame.getGenre());
			consoleGame.setHardwarePlatform(newConsoleGame.getHardwarePlatform());
			consoleGame.setProducer(newConsoleGame.getProducer());
			consoleGame.setPublisher(newConsoleGame.getPublisher());
			consoleGame.setLanguage(newConsoleGame.getLanguage());
			return consoleGameAssembler.toModel(consoleGameRepository.save(consoleGame));
		}).orElseGet(() -> {
			newConsoleGame.setId(id);
			return consoleGameAssembler.toModel(consoleGameRepository.save(newConsoleGame));
		}));
	}
	
	@GetMapping("/example")
	public ResponseEntity<EntityModel<ConsoleGame>> example(){
		return ResponseEntity.ok(consoleGameAssembler.toModel(EXAMPLE));
	}
}
