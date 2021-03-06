package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.dabrowski.GameShop.entities.ConsoleGame;

@Repository
public interface ConsoleGameRepository extends CrudRepository<ConsoleGame, Integer> {

}
