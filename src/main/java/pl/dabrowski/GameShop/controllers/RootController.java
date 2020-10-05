package pl.dabrowski.GameShop.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;

@RestController("/")
public class RootController {
	public static final String DEFAULT_EXAMPLE_VALUE = "*";
	
	@RequiredArgsConstructor
	@Getter
	public static class Constants {
		private final String defaultExampleValue;
	}

	@GetMapping
	public ResponseEntity<EntityModel<Constants>> getRestAPIroot() {
		return ResponseEntity.ok(EntityModel.of(new Constants(DEFAULT_EXAMPLE_VALUE),
				linkTo(methodOn(ConsoleGameController.class).findAll()).withRel("consoleGames"),
				linkTo(methodOn(AgeCategoryController.class).findAll()).withRel("ageCategories"),
				linkTo(methodOn(GameplayModeController.class).findAll()).withRel("gameplayModes"),
				linkTo(methodOn(GenreController.class).findAll()).withRel("genres"),
				linkTo(methodOn(HardwarePlatformController.class).findAll()).withRel("hardwarePlatforms"),
				linkTo(methodOn(LanguageController.class).findAll()).withRel("languages"),
				linkTo(methodOn(ProducerController.class).findAll()).withRel("producers"),
				linkTo(methodOn(PublisherController.class).findAll()).withRel("publishers")));
	}
}
