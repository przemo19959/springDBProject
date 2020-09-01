package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.dabrowski.GameShop.entities.GameplayMode;

@Repository
public interface GameplayModeRepository extends CrudRepository<GameplayMode, Integer> {

}
