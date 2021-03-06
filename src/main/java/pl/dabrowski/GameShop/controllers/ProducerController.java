package pl.dabrowski.GameShop.controllers;

import static pl.dabrowski.GameShop.controllers.RootController.DEFAULT_EXAMPLE_VALUE;

import java.util.NoSuchElementException;

import javax.validation.Valid;

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

import pl.dabrowski.GameShop.assemblers.ProducerAssembler;
import pl.dabrowski.GameShop.entities.Producer;
import pl.dabrowski.GameShop.repositories.ProducerRepository;

@RestController
@RequestMapping(ProducerController.BASE_URL)
public class ProducerController {
	final static String BASE_URL = "/producers";
	private final ProducerRepository producerRepository;
	private final ProducerAssembler producerAssembler;

	public static final Producer EXAMPLE;
	static {
		EXAMPLE = new Producer();
		EXAMPLE.setName(DEFAULT_EXAMPLE_VALUE);
	}

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

	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<Producer>> update(@Valid @RequestBody Producer newProducer, @PathVariable int id) {
		return ResponseEntity.ok(producerRepository.findById(id).map(producer -> {
			producer.setName(newProducer.getName());
			return producerAssembler.toModel(producerRepository.save(producer));
		}).orElseThrow(NoSuchElementException::new));
	}

	@PostMapping
	public ResponseEntity<EntityModel<Producer>> save(@Valid @RequestBody Producer newProducer) {
		return new ResponseEntity<>(producerAssembler.toModel(producerRepository.save(newProducer)),
				HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		producerRepository.delete(producerRepository.findById(id).orElseThrow(NoSuchElementException::new));
		return ResponseEntity.ok().build();
	}

	@GetMapping("/example")
	public ResponseEntity<EntityModel<Producer>> example() {
		return ResponseEntity.ok(producerAssembler.toModel(EXAMPLE));
	}
}
