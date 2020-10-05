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
import pl.dabrowski.GameShop.assemblers.GenreAssembler;
import pl.dabrowski.GameShop.entities.Genre;
import pl.dabrowski.GameShop.repositories.GenreRepository;

import static pl.dabrowski.GameShop.controllers.RootController.DEFAULT_EXAMPLE_VALUE;

import java.util.NoSuchElementException;

import javax.validation.Valid;

@RestController
@RequestMapping(GenreController.BASE_URL)
public class GenreController {
	final static String BASE_URL = "/genres";
	private final GenreRepository genreRepository;
	private final GenreAssembler genreAssembler;

	public static final Genre EXAMPLE;
	static {
		EXAMPLE = new Genre();
		EXAMPLE.setName(DEFAULT_EXAMPLE_VALUE);
	}

	@Autowired
	public GenreController(GenreRepository myRepository, GenreAssembler genreAssembler) {
		this.genreRepository = myRepository;
		this.genreAssembler = genreAssembler;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<Genre>>> findAll() {
		return ResponseEntity.ok(genreAssembler.toCollectionModel(genreRepository.findAll()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Genre>> findById(@PathVariable int id) {
		return ResponseEntity.ok(genreAssembler.toModel(genreRepository.findById(id)//
				.orElseThrow(NoSuchElementException::new)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<Genre>> update(@Valid @RequestBody Genre newGenre, @PathVariable int id) {
		return ResponseEntity.ok(genreRepository.findById(id).map(genre -> {
			genre.setName(newGenre.getName());
			return genreAssembler.toModel(genreRepository.save(genre));
		}).orElseThrow(NoSuchElementException::new));
	}

	@PostMapping
	public ResponseEntity<EntityModel<Genre>> save(@Valid @RequestBody Genre newGenre) {
		return new ResponseEntity<>(genreAssembler.toModel(genreRepository.save(newGenre)), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		genreRepository.delete(genreRepository.findById(id).orElseThrow(NoSuchElementException::new));
		return ResponseEntity.ok().build();
	}

	@GetMapping("/example")
	public ResponseEntity<EntityModel<Genre>> example() {
		return ResponseEntity.ok(genreAssembler.toModel(EXAMPLE));
	}
}
