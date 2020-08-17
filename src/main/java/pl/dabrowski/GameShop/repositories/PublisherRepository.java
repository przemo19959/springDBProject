package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;

import pl.dabrowski.GameShop.entities.Publisher;
import pl.dabrowski.GameShop.repositories.custom.MyRepository;

public interface PublisherRepository extends CrudRepository<Publisher, Integer>,MyRepository<Publisher> {

}
