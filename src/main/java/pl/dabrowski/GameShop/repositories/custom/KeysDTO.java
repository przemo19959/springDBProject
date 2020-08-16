package pl.dabrowski.GameShop.repositories.custom;

import lombok.Data;

@Data
public class KeysDTO {
    private String tableName;
    private String columnName;
    private String refTableName;
    private String refColumnName;
}
