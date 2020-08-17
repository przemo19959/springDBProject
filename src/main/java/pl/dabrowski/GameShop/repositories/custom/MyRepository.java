package pl.dabrowski.GameShop.repositories.custom;


public interface MyRepository<T> {
    Iterable<KeyDTO> findKeys(Class<T> tClass);
}
