package pl.dabrowski.GameShop.repositories.custom;

import lombok.Data;

@Data
public class KeyDTO {
    private final String tableName;
    private final String columnName;
    private final String refTableName;
    private final String refColumnName;
}
