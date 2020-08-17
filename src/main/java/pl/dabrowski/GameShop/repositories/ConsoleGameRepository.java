package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.dabrowski.GameShop.entities.ConsoleGame;
import pl.dabrowski.GameShop.repositories.custom.MyRepository;

public interface ConsoleGameRepository extends CrudRepository<ConsoleGame, Integer>,MyRepository<ConsoleGame> {

}
