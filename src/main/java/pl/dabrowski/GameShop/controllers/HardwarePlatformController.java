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

import pl.dabrowski.GameShop.assemblers.HardwarePlatformAssembler;
import pl.dabrowski.GameShop.entities.HardwarePlatform;
import pl.dabrowski.GameShop.repositories.HardwarePlatformRepository;

@RestController
@RequestMapping(HardwarePlatformController.BASE_URL)
public class HardwarePlatformController {
	final static String BASE_URL = "/hardwarePlatforms";
	private final HardwarePlatformRepository hardwarePlatformRepository;
	private final HardwarePlatformAssembler hardwarePlatformAssembler;

	public static final HardwarePlatform EXAMPLE;
	static {
		EXAMPLE = new HardwarePlatform();
		EXAMPLE.setName(DEFAULT_EXAMPLE_VALUE);
		EXAMPLE.setShortcut(DEFAULT_EXAMPLE_VALUE);
	}

	@Autowired
	public HardwarePlatformController(HardwarePlatformRepository hardwarePlatformRepository,
			HardwarePlatformAssembler hardwarePlatformAssembler) {
		this.hardwarePlatformRepository = hardwarePlatformRepository;
		this.hardwarePlatformAssembler = hardwarePlatformAssembler;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<HardwarePlatform>>> findAll() {
		return ResponseEntity.ok(hardwarePlatformAssembler.toCollectionModel(hardwarePlatformRepository.findAll()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<HardwarePlatform>> findById(@PathVariable int id) {
		return ResponseEntity.ok(hardwarePlatformAssembler.toModel(hardwarePlatformRepository.findById(id)//
				.orElseThrow(NoSuchElementException::new)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<HardwarePlatform>> update(@Valid @RequestBody HardwarePlatform newHardwarePlatform,
			@PathVariable int id) {
		return ResponseEntity.ok(hardwarePlatformRepository.findById(id).map(hardwarePlatform -> {
			hardwarePlatform.setName(newHardwarePlatform.getName());
			return hardwarePlatformAssembler.toModel(hardwarePlatformRepository.save(hardwarePlatform));
		}).orElseThrow(NoSuchElementException::new));
	}

	@PostMapping
	public ResponseEntity<EntityModel<HardwarePlatform>> save(@Valid @RequestBody HardwarePlatform newHardwarePlatform) {
		return new ResponseEntity<>(
				hardwarePlatformAssembler.toModel(hardwarePlatformRepository.save(newHardwarePlatform)),
				HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		hardwarePlatformRepository
				.delete(hardwarePlatformRepository.findById(id).orElseThrow(NoSuchElementException::new));
		return ResponseEntity.ok().build();
	}

	@GetMapping("/example")
	public ResponseEntity<EntityModel<HardwarePlatform>> example() {
		return ResponseEntity.ok(hardwarePlatformAssembler.toModel(EXAMPLE));
	}
}
