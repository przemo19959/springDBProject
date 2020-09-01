package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.dabrowski.GameShop.entities.Language;

@Repository
public interface LanguageRepository extends  CrudRepository<Language,Integer> {

}
