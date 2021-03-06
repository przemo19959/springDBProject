package pl.dabrowski.GameShop.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import pl.dabrowski.GameShop.controllers.AgeCategoryController;
import pl.dabrowski.GameShop.entities.AgeCategory;

@Component
public class AgeCategoryAssembler implements RepresentationModelAssembler<AgeCategory, EntityModel<AgeCategory>> {
    @Override
    public EntityModel<AgeCategory> toModel(AgeCategory ageCategory) {
        return EntityModel.of(ageCategory,
                linkTo(methodOn(AgeCategoryController.class).findById(ageCategory.getId())).withSelfRel(),
                linkTo(methodOn(AgeCategoryController.class).findAll()).withRel("all"));
    }

    @Override
    public CollectionModel<EntityModel<AgeCategory>> toCollectionModel(Iterable<? extends AgeCategory> ageCategories) {
        return CollectionModel.of(StreamSupport.stream(ageCategories.spliterator(), false)
                        .map(this::toModel)
                        .collect(Collectors.toList()),
                linkTo(methodOn(AgeCategoryController.class).findAll()).withSelfRel(),
				linkTo(methodOn(AgeCategoryController.class).example()).withRel("example"));
    }
}
