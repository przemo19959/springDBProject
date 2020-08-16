package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import pl.dabrowski.GameShop.entities.Genre;

@RepositoryRestResource
public interface GenreRepository extends CrudRepository<Genre, Integer> {

}
