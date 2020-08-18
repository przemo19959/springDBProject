package pl.dabrowski.GameShop.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.dabrowski.GameShop.controllers.*;
import pl.dabrowski.GameShop.entities.ConsoleGame;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ConsoleGameAssembler implements RepresentationModelAssembler<ConsoleGame, EntityModel<ConsoleGame>> {
    @Override
    public EntityModel<ConsoleGame> toModel(ConsoleGame consoleGame) {
        return EntityModel.of(consoleGame,
                linkTo(methodOn(ConsoleGameController.class).findById(consoleGame.getId())).withSelfRel(),

                linkTo(methodOn(PublisherController.class).findById(consoleGame.getPublisher().getId())).withRel("publisher"),
                linkTo(methodOn(AgeCategoryController.class).findById(consoleGame.getAgeCategory().getId())).withRel("ageCategory"),
                linkTo(methodOn(GameplayModeController.class).findById(consoleGame.getGameplayMode().getId())).withRel("gameplayMode"),
                linkTo(methodOn(GenreController.class).findById(consoleGame.getGenre().getId())).withRel("genre"),
                linkTo(methodOn(HardwarePlatformController.class).findById(consoleGame.getHardwarePlatform().getId())).withRel("hardwarePlatform"),
                linkTo(methodOn(LanguageController.class).findById(consoleGame.getLanguage().getId())).withRel("language"),
                linkTo(methodOn(ProducerController.class).findById(consoleGame.getProducer().getId())).withRel("producer"));
    }

    @Override
    public CollectionModel<EntityModel<ConsoleGame>> toCollectionModel(Iterable<? extends ConsoleGame> consoleGames) {
        return CollectionModel.of(StreamSupport.stream(consoleGames.spliterator(), false)
                        .map(this::toModel)
                        .collect(Collectors.toList()),
                linkTo(methodOn(ConsoleGameController.class).findAll()).withSelfRel());
    }
}
