package pl.dabrowski.GameShop.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.dabrowski.GameShop.controllers.LanguageController;
import pl.dabrowski.GameShop.entities.Language;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LanguageAssembler implements RepresentationModelAssembler<Language, EntityModel<Language>> {
    @Override
    public EntityModel<Language> toModel(Language language) {
        return EntityModel.of(language,
                linkTo(methodOn(LanguageController.class).findById(language.getId())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<Language>> toCollectionModel(Iterable<? extends Language> languages) {
        return CollectionModel.of(StreamSupport.stream(languages.spliterator(), false)
                        .map(this::toModel)
                        .collect(Collectors.toList()),
                linkTo(methodOn(LanguageController.class).findAll()).withSelfRel());
    }
}
