package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import pl.dabrowski.GameShop.entities.Producer;
import pl.dabrowski.GameShop.repositories.custom.MyRepository;

public interface ProducerRepository extends CrudRepository<Producer, Integer>,MyRepository<Producer> {

}
