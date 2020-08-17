package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.dabrowski.GameShop.entities.Language;
import pl.dabrowski.GameShop.repositories.custom.MyRepository;

public interface LanguageRepository extends CrudRepository<Language, Integer>, MyRepository<Language> {

}
