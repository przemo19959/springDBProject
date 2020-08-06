package application.dao;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.SneakyThrows;

@Repository
public class Dao {
	// queries
	private static final String FIND_ALL = "SELECT * FROM {0};";
	private static final String FIND_BY_ID = "SELECT * FROM {0} WHERE id= {1};";
	private static final String FIND_ALL_COLUMNS = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS "
			+ "WHERE TABLE_NAME = ''{0}'' ORDER BY ORDINAL_POSITION;";

	@Autowired
	private TransactionWrapper transactionWrapper;

	// CRUD operations
	// C-Create
	public <T> int save(T entity) {
		return transactionWrapper.getTransactionResult(session -> {
			System.out.println(getPrimaryKeyValue(entity));
			session.save(entity);
			return getPrimaryKeyValue(entity);
		}, Integer.class);
	}

	// R-Retrieve
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> resultType) {
		return transactionWrapper.getTransactionResult(
				session -> session
						.createNativeQuery(MessageFormat.format(FIND_ALL, getTableName(resultType)), resultType).list(),
				List.class);
	}

	@SuppressWarnings("unchecked")
	public <T> Optional<T> findById(int id, Class<T> resultType) {
		return transactionWrapper.getTransactionResult(session -> session
				.createNativeQuery(MessageFormat.format(FIND_BY_ID, getTableName(resultType), id), resultType)
				.uniqueResultOptional(), Optional.class);
	}

	// U-Update
	public <T> void update(T entity) {
		transactionWrapper.performTransaction(session -> session.update(entity));
	}

	// D-Delete
	public <T> void delete(T entity) {
		transactionWrapper.performTransaction(session -> session.delete(entity));
	}

	// Other methods
	public <T> List<String> getColumnNames(Class<T> entityClass) {
		List<String> result=new ArrayList<>();
		for(Field field:entityClass.getDeclaredFields()) {
			if(field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(Column.class)) {
				result.add(field.getName());
			}else if(field.isAnnotationPresent(JoinColumn.class)) {
				result.add("fk_"+field.getName());
			}
		}
		return result;
	}
	
//	public <T> List<String> getForeignColumnNames(Class<T> entityClass){
//		List<String> result=new ArrayList<>();
//		for(Field field:entityClass.getDeclaredFields()) {
//			if(field.isAnnotationPresent(JoinColumn.class)) {
//				result.add(field.getName());
//			}
//		}
//		return result;
//	}
	
	private <T> String getTableName(Class<T> type) {
		if (type.isAnnotationPresent(Table.class))
			return type.getAnnotation(Table.class).name();
		return "";
	}

	@SneakyThrows(value = IllegalAccessException.class)
	private <T> int getPrimaryKeyValue(T entity) {
		for (Field field : entity.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Id.class)) {
				field.setAccessible(true);
				return field.getInt(entity);
			}
		}
		return -1;
	}
}
 