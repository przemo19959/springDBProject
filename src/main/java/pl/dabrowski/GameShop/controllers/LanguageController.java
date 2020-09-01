package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dabrowski.GameShop.assemblers.LanguageAssembler;
import pl.dabrowski.GameShop.entities.Language;
import pl.dabrowski.GameShop.repositories.LanguageRepository;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(LanguageController.BASE_URL)
public class LanguageController {
	final static String BASE_URL = "/languages";
	private final LanguageRepository languageRepository;
	private final LanguageAssembler languageAssembler;

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
}
