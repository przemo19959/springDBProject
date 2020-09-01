package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dabrowski.GameShop.assemblers.ProducerAssembler;
import pl.dabrowski.GameShop.entities.Producer;
import pl.dabrowski.GameShop.repositories.ProducerRepository;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(ProducerController.BASE_URL)
public class ProducerController {
	final static String BASE_URL = "/producers";
	private final ProducerRepository producerRepository;
	private final ProducerAssembler producerAssembler;

	@Autowired
	public ProducerController(ProducerRepository producerRepository, ProducerAssembler producerAssembler) {
		this.producerRepository = producerRepository;
		this.producerAssembler = producerAssembler;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<Producer>>> findAll() {
		return ResponseEntity.ok(producerAssembler.toCollectionModel(producerRepository.findAll()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Producer>> findById(@PathVariable int id) {
		return ResponseEntity.ok(producerAssembler.toModel(producerRepository.findById(id)//
				.orElseThrow(NoSuchElementException::new)));
	}
}
