package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.dabrowski.GameShop.entities.Genre;
import pl.dabrowski.GameShop.repositories.GenreRepository;
import pl.dabrowski.GameShop.repositories.custom.KeyDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RepositoryRestController
@RequestMapping(GenreController.BASE_URL)
public class GenreController {
    final static String BASE_URL = "/genres";
    private final GenreRepository genreRepository;

    @Autowired
    public GenreController(GenreRepository myRepository) {
        this.genreRepository = myRepository;
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<?> search() {
        return ResponseEntity.ok(EntityModel.of("",
                linkTo(methodOn(GenreController.class).findKeys()).withRel("keys")));
    }

    @GetMapping("/search/findKeys")
    @ResponseBody
    public ResponseEntity<Iterable<KeyDTO>> findKeys() {
        return ResponseEntity.ok(genreRepository.findKeys(Genre.class));
    }
}
