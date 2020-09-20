package pl.dabrowski.GameShop.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import pl.dabrowski.GameShop.controllers.PublisherController;
import pl.dabrowski.GameShop.entities.Publisher;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PublisherAssembler implements RepresentationModelAssembler<Publisher, EntityModel<Publisher>> {
	@Override
	public EntityModel<Publisher> toModel(Publisher publisher) {
		return EntityModel.of(publisher, //
				linkTo(methodOn(PublisherController.class).findById(publisher.getId())).withSelfRel(),
				linkTo(methodOn(PublisherController.class).findAll()).withRel("all"));
	}

	@Override
	public CollectionModel<EntityModel<Publisher>> toCollectionModel(Iterable<? extends Publisher> publishers) {
		return CollectionModel.of(StreamSupport.stream(publishers.spliterator(), false)//
				.map(this::toModel)//
				.collect(Collectors.toList()), //
				linkTo(methodOn(PublisherController.class).findAll()).withSelfRel(),
				linkTo(methodOn(PublisherController.class).example()).withRel("example"));
	}
}
