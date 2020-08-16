package pl.dabrowski.GameShop.repositories.custom;

import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

public interface CustomRepository<T,ID extends Serializable> extends CrudRepository<T,ID> {
    public KeysDTO getKeysDto();
}
