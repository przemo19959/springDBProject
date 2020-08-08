package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;

import pl.dabrowski.GameShop.entities.AgeCategory;

public interface AgeCategoryRepository extends CrudRepository<AgeCategory, Integer> {

}
