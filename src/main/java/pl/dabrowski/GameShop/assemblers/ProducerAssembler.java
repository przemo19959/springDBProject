package pl.dabrowski.GameShop.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import pl.dabrowski.GameShop.controllers.ProducerController;
import pl.dabrowski.GameShop.entities.Producer;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProducerAssembler implements RepresentationModelAssembler<Producer, EntityModel<Producer>> {
	@Override
	public EntityModel<Producer> toModel(Producer producer) {
		return EntityModel.of(producer,
				linkTo(methodOn(ProducerController.class).findById(producer.getId())).withSelfRel(),
				linkTo(methodOn(ProducerController.class).findAll()).withRel("all"));
	}

	@Override
	public CollectionModel<EntityModel<Producer>> toCollectionModel(Iterable<? extends Producer> producers) {
		return CollectionModel.of(
				StreamSupport.stream(producers.spliterator(), false).map(this::toModel).collect(Collectors.toList()),
				linkTo(methodOn(ProducerController.class).findAll()).withSelfRel(),
				linkTo(methodOn(ProducerController.class).example()).withRel("example"));
	}
}
