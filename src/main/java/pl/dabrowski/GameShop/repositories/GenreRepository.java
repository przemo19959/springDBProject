package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;

import pl.dabrowski.GameShop.entities.Genre;

public interface GenreRepository extends CrudRepository<Genre, Integer> {

}
