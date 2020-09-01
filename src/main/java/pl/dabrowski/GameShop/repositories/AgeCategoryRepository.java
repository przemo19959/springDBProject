package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.dabrowski.GameShop.entities.AgeCategory;

@Repository
public interface AgeCategoryRepository extends CrudRepository<AgeCategory, Integer> {

}
