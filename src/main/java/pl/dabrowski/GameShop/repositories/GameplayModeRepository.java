package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.dabrowski.GameShop.entities.GameplayMode;

public interface GameplayModeRepository extends CrudRepository<GameplayMode, Integer> {

}
