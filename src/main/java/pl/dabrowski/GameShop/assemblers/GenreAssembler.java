package pl.dabrowski.GameShop.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import pl.dabrowski.GameShop.controllers.GenreController;
import pl.dabrowski.GameShop.entities.Genre;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GenreAssembler implements RepresentationModelAssembler<Genre, EntityModel<Genre>> {
	@Override
	public EntityModel<Genre> toModel(Genre genre) {
		return EntityModel.of(genre, //
				linkTo(methodOn(GenreController.class).findById(genre.getId())).withSelfRel(),
				linkTo(methodOn(GenreController.class).findAll()).withRel("all"));
	}

	@Override
	public CollectionModel<EntityModel<Genre>> toCollectionModel(Iterable<? extends Genre> genres) {
		return CollectionModel.of(
				StreamSupport.stream(genres.spliterator(), false).map(this::toModel).collect(Collectors.toList()),
				linkTo(methodOn(GenreController.class).findAll()).withSelfRel(),
				linkTo(methodOn(GenreController.class).example()).withRel("example"));
	}
}
