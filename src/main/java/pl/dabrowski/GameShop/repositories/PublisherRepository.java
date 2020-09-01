package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.dabrowski.GameShop.entities.Publisher;

@Repository
public interface PublisherRepository extends CrudRepository<Publisher, Integer> {

}
