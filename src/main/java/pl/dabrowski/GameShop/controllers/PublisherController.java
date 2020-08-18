package pl.dabrowski.GameShop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dabrowski.GameShop.assemblers.PublisherAssembler;
import pl.dabrowski.GameShop.repositories.PublisherRepository;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(PublisherController.BASE_URL)
public class PublisherController {
    final static String BASE_URL = "/publishers";
    private final PublisherRepository publisherRepository;
    private final PublisherAssembler publisherAssembler;

    @Autowired
    public PublisherController(PublisherRepository publisherRepository, PublisherAssembler publisherAssembler) {
        this.publisherRepository = publisherRepository;
        this.publisherAssembler = publisherAssembler;
    }

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(publisherAssembler.toCollectionModel(publisherRepository.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id){
        return ResponseEntity.ok(publisherAssembler.toModel(publisherRepository.findById(id).orElseThrow(NoSuchElementException::new)));
    }
//
//    @GetMapping("/search")
//    @ResponseBody
//    public ResponseEntity<?> search() {
//        return ResponseEntity.ok(EntityModel.of("",
//                linkTo(methodOn(PublisherController.class).findKeys()).withRel("keys")));
//    }
//
//    @GetMapping("/search/findKeys")
//    @ResponseBody
//    public ResponseEntity<Iterable<KeyDTO>> findKeys() {
//        return ResponseEntity.ok(publisherRepository.findKeys(Publisher.class));
//    }
}