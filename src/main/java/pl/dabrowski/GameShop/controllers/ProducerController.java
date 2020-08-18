package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dabrowski.GameShop.assemblers.ProducerAssembler;
import pl.dabrowski.GameShop.repositories.ProducerRepository;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(ProducerController.BASE_URL)
public class ProducerController {
    final static String BASE_URL = "/producers";
    private final ProducerRepository producerRepository;
    private final ProducerAssembler producerAssembler;

    @Autowired
    public ProducerController(ProducerRepository producerRepository, ProducerAssembler producerAssembler) {
        this.producerRepository = producerRepository;
        this.producerAssembler = producerAssembler;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(producerAssembler.toCollectionModel(producerRepository.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        return ResponseEntity.ok(producerAssembler.toModel(producerRepository.findById(id).orElseThrow(NoSuchElementException::new)));
    }
//
//    @GetMapping("/search")
//    @ResponseBody
//    public ResponseEntity<?> search() {
//        return ResponseEntity.ok(EntityModel.of("",
//                linkTo(methodOn(ProducerController.class).findKeys()).withRel("keys")));
//    }
//
//    @GetMapping("/search/findKeys")
//    @ResponseBody
//    public ResponseEntity<Iterable<KeyDTO>> findKeys() {
//        return ResponseEntity.ok(producerRepository.findKeys(Producer.class));
//    }
}
