package pl.dabrowski.GameShop.controllers;

import static pl.dabrowski.GameShop.controllers.RootController.DEFAULT_EXAMPLE_VALUE;

import java.util.NoSuchElementException;

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

import pl.dabrowski.GameShop.assemblers.LanguageAssembler;
import pl.dabrowski.GameShop.entities.Language;
import pl.dabrowski.GameShop.repositories.LanguageRepository;

@RestController
@RequestMapping(LanguageController.BASE_URL)
public class LanguageController {
	final static String BASE_URL = "/languages";
	private final LanguageRepository languageRepository;
	private final LanguageAssembler languageAssembler;
	
	public static final Language EXAMPLE;
	static {
		EXAMPLE=new Language();
		EXAMPLE.setName(DEFAULT_EXAMPLE_VALUE);
		EXAMPLE.setShortcut(DEFAULT_EXAMPLE_VALUE);
	}

	@Autowired
	public LanguageController(LanguageRepository languageRepository, LanguageAssembler languageAssembler) {
		this.languageRepository = languageRepository;
		this.languageAssembler = languageAssembler;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<Language>>> findAll() {
		return ResponseEntity.ok(languageAssembler.toCollectionModel(languageRepository.findAll()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Language>> findById(@PathVariable int id) {
		return ResponseEntity.ok(languageAssembler.toModel(languageRepository.findById(id)//
				.orElseThrow(NoSuchElementException::new)));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<Language>> update(@RequestBody Language newLanguage, @PathVariable int id) {
		return ResponseEntity.ok(languageRepository.findById(id).map(language -> {
			language.setName(newLanguage.getName());
			return languageAssembler.toModel(languageRepository.save(language));
		}).orElseThrow(NoSuchElementException::new));
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<Language>> save(@RequestBody Language newLanguage){
		return new ResponseEntity<>(languageAssembler.toModel(languageRepository.save(newLanguage)), HttpStatus.CREATED); 
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		languageRepository.delete(languageRepository.findById(id).orElseThrow(NoSuchElementException::new));
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/example")
	public ResponseEntity<EntityModel<Language>> example(){
		return ResponseEntity.ok(languageAssembler.toModel(EXAMPLE));
	}
}
