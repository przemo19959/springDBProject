package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;

import pl.dabrowski.GameShop.entities.AgeCategory;
import pl.dabrowski.GameShop.repositories.custom.MyRepository;

public interface AgeCategoryRepository extends CrudRepository<AgeCategory, Integer>,MyRepository<AgeCategory> {

}
