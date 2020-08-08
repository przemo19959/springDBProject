package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.dabrowski.GameShop.entities.ConsoleGame;

public interface ConsoleGameRepository extends CrudRepository<ConsoleGame, Integer> {

}
