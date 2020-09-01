package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.dabrowski.GameShop.entities.Genre;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Integer> {

}
