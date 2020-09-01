package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.dabrowski.GameShop.entities.Producer;

@Repository
public interface ProducerRepository extends CrudRepository<Producer, Integer> {

}
