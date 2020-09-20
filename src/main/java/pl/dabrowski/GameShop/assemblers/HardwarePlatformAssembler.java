package pl.dabrowski.GameShop.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import pl.dabrowski.GameShop.controllers.HardwarePlatformController;
import pl.dabrowski.GameShop.entities.HardwarePlatform;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HardwarePlatformAssembler
		implements RepresentationModelAssembler<HardwarePlatform, EntityModel<HardwarePlatform>> {
	@Override
	public EntityModel<HardwarePlatform> toModel(HardwarePlatform hardwarePlatform) {
		return EntityModel.of(hardwarePlatform, //
				linkTo(methodOn(HardwarePlatformController.class).findById(hardwarePlatform.getId())).withSelfRel(),
				linkTo(methodOn(HardwarePlatformController.class).findAll()).withRel("all"));
	}

	@Override
	public CollectionModel<EntityModel<HardwarePlatform>> toCollectionModel(
			Iterable<? extends HardwarePlatform> hardwarePlatforms) {
		return CollectionModel.of(
				StreamSupport.stream(hardwarePlatforms.spliterator(), false).map(this::toModel)
						.collect(Collectors.toList()),
				linkTo(methodOn(HardwarePlatformController.class).findAll()).withSelfRel(),
				linkTo(methodOn(HardwarePlatformController.class).example()).withRel("example"));
	}
}
