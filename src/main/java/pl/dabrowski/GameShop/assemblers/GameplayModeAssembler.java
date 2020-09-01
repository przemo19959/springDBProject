package pl.dabrowski.GameShop.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.dabrowski.GameShop.controllers.GameplayModeController;
import pl.dabrowski.GameShop.entities.GameplayMode;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GameplayModeAssembler implements RepresentationModelAssembler<GameplayMode, EntityModel<GameplayMode>> {
    @Override
    public EntityModel<GameplayMode> toModel(final GameplayMode gameplayMode) {
        return EntityModel.of(gameplayMode,
                linkTo(methodOn(GameplayModeController.class).findById(gameplayMode.getId())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<GameplayMode>> toCollectionModel(
            final Iterable<? extends GameplayMode> gameplayModes) {
        return CollectionModel.of(StreamSupport.stream(gameplayModes.spliterator(), false)
                        .map(this::toModel)
                        .collect(Collectors.toList()),
                linkTo(methodOn(GameplayModeController.class).findAll()).withSelfRel());
    }
}
