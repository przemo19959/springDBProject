package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import pl.dabrowski.GameShop.entities.Genre;
import pl.dabrowski.GameShop.repositories.custom.KeyDTO;
import pl.dabrowski.GameShop.repositories.custom.MyRepository;

@RepositoryRestResource
public interface GenreRepository extends CrudRepository<Genre, Integer>, MyRepository<Genre> {

}
