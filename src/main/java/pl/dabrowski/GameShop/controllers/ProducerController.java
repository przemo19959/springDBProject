package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.dabrowski.GameShop.entities.Producer;
import pl.dabrowski.GameShop.repositories.ProducerRepository;
import pl.dabrowski.GameShop.repositories.custom.KeyDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RepositoryRestResource
@RequestMapping(ProducerController.BASE_URL)
public class ProducerController {
    final static String BASE_URL = "/producers";
    private final ProducerRepository producerRepository;

    @Autowired
    public ProducerController(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<?> search() {
        return ResponseEntity.ok(EntityModel.of("",
                linkTo(methodOn(ProducerController.class).findKeys()).withRel("keys")));
    }

    @GetMapping("/search/findKeys")
    @ResponseBody
    public ResponseEntity<Iterable<KeyDTO>> findKeys() {
        return ResponseEntity.ok(producerRepository.findKeys(Producer.class));
    }
}
