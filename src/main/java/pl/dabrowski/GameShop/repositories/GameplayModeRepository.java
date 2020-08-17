package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;

import pl.dabrowski.GameShop.entities.GameplayMode;
import pl.dabrowski.GameShop.repositories.custom.MyRepository;

public interface GameplayModeRepository extends CrudRepository<GameplayMode, Integer>,MyRepository<GameplayMode> {

}
