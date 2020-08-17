package pl.dabrowski.GameShop.repositories;

import org.springframework.data.repository.CrudRepository;

import pl.dabrowski.GameShop.entities.HardwarePlatform;
import pl.dabrowski.GameShop.repositories.custom.MyRepository;

public interface HardwarePlatformRepository extends CrudRepository<HardwarePlatform, Integer>,MyRepository<HardwarePlatform> {

}
