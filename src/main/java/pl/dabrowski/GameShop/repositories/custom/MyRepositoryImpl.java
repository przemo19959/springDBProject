package pl.dabrowski.GameShop.repositories.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.Table;

@Component
public class MyRepositoryImpl<T> implements MyRepository<T> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MyRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<KeyDTO> findKeys(Class<T> tClass) {
        return jdbcTemplate.query("SELECT TABLE_NAME,COLUMN_NAME,REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME" +
                        " FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE" +
                        " WHERE REFERENCED_TABLE_SCHEMA = 'gameapp'" +
                        " AND TABLE_NAME = ?",
                new Object[]{tClass.getAnnotation(Table.class).name()},
                (rs, rowNum) ->
                        new KeyDTO(rs.getString("TABLE_NAME"),
                                rs.getString("COLUMN_NAME"),
                                rs.getString("REFERENCED_TABLE_NAME"),
                                rs.getString("REFERENCED_COLUMN_NAME"))

        );
        // TODO: 2020-08-16 dodać parametr z nazwą tabeli oraz z nazwą bazy danych (z properties)
    }
}
