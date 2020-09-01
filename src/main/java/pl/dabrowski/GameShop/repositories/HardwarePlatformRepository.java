package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.dabrowski.GameShop.entities.HardwarePlatform;

@Repository
public interface HardwarePlatformRepository extends CrudRepository<HardwarePlatform, Integer> {

}
