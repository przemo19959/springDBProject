package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dabrowski.GameShop.assemblers.AgeCategoryAssembler;
import pl.dabrowski.GameShop.entities.AgeCategory;
import pl.dabrowski.GameShop.repositories.AgeCategoryRepository;

import static pl.dabrowski.GameShop.controllers.RootController.DEFAULT_EXAMPLE_VALUE;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(AgeCategoryController.BASE_URL)
public class AgeCategoryController {
	final static String BASE_URL = "/ageCategories";
	private final AgeCategoryRepository ageCategoryRepository;
	private final AgeCategoryAssembler ageCategoryAssembler;
	
	public static final AgeCategory EXAMPLE;
	static {
		EXAMPLE=new AgeCategory();
		EXAMPLE.setName(DEFAULT_EXAMPLE_VALUE);
	}

	@Autowired
	public AgeCategoryController(AgeCategoryRepository myRepository, AgeCategoryAssembler ageCategoryAssembler) {
		this.ageCategoryRepository = myRepository;
		this.ageCategoryAssembler = ageCategoryAssembler;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<AgeCategory>>> findAll() {
		return ResponseEntity.ok(ageCategoryAssembler.toCollectionModel(ageCategoryRepository.findAll()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<AgeCategory>> findById(@PathVariable int id) {
		return ResponseEntity.ok(ageCategoryAssembler.toModel(ageCategoryRepository.findById(id)//
				.orElseThrow(NoSuchElementException::new)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<AgeCategory>> update(@RequestBody AgeCategory newAgeCategory,
			@PathVariable int id) {
		return ResponseEntity.ok(ageCategoryRepository.findById(id).map(ageCategory -> {
			ageCategory.setName(newAgeCategory.getName());
			return ageCategoryAssembler.toModel(ageCategoryRepository.save(ageCategory));
		}).orElseGet(() -> {
			newAgeCategory.setId(id);
			return ageCategoryAssembler.toModel(ageCategoryRepository.save(newAgeCategory));
		}));
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<AgeCategory>> save(@RequestBody AgeCategory newAgeCategory){
		return new ResponseEntity<>(ageCategoryAssembler.toModel(ageCategoryRepository.save(newAgeCategory)), HttpStatus.CREATED); 
	}
	
	@GetMapping("/example")
	public ResponseEntity<EntityModel<AgeCategory>> example(){
		return ResponseEntity.ok(ageCategoryAssembler.toModel(EXAMPLE));
	}
}
