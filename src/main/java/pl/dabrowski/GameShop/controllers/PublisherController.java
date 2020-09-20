package pl.dabrowski.GameShop.controllers;

import java.util.NoSuchElementException;

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

import pl.dabrowski.GameShop.assemblers.PublisherAssembler;
import pl.dabrowski.GameShop.entities.Publisher;
import pl.dabrowski.GameShop.repositories.PublisherRepository;

@RestController
@RequestMapping(PublisherController.BASE_URL)
public class PublisherController {
	final static String BASE_URL = "/publishers";
	private final PublisherRepository publisherRepository;
	private final PublisherAssembler publisherAssembler;
	
	public static final Publisher EXAMPLE;
	static {
		EXAMPLE=new Publisher();
		EXAMPLE.setName("fill me");
	}

	@Autowired
	public PublisherController(PublisherRepository publisherRepository, PublisherAssembler publisherAssembler) {
		this.publisherRepository = publisherRepository;
		this.publisherAssembler = publisherAssembler;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<Publisher>>> findAll() {
		return ResponseEntity.ok(publisherAssembler.toCollectionModel(publisherRepository.findAll()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Publisher>> findById(@PathVariable int id) {
		return ResponseEntity.ok(publisherAssembler.toModel(publisherRepository.findById(id)//
				.orElseThrow(NoSuchElementException::new)));
	}

	@PutMapping(value = "{/id}")
	public ResponseEntity<EntityModel<Publisher>> update(@RequestBody Publisher newPublisher, @PathVariable int id) {
		return ResponseEntity.ok(publisherRepository.findById(id).map(language -> {
			language.setName(newPublisher.getName());
			return publisherAssembler.toModel(publisherRepository.save(language));
		}).orElseGet(() -> {
			newPublisher.setId(id);
			return publisherAssembler.toModel(publisherRepository.save(newPublisher));
		}));
	}
	
	@GetMapping("/example")
	public ResponseEntity<EntityModel<Publisher>> example(){
		return ResponseEntity.ok(publisherAssembler.toModel(EXAMPLE));
	}
}
