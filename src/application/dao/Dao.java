package application.dao;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.SneakyThrows;

@Repository
public class Dao {
	// queries
	private static final String FIND_ALL = "SELECT * FROM {0};";
	private static final String FIND_BY_ID = "SELECT * FROM {0} WHERE id= {1};";

	@Autowired
	private TransactionWrapper transactionWrapper;
	
	// CRUD operations
	// C-Create
	public <T> int save(T entity) {
		return transactionWrapper.getTransactionResult(session -> {
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
		return transactionWrapper.getTransactionResult(session->session.createNativeQuery(MessageFormat.format(FIND_BY_ID, getTableName(resultType), id), resultType)
				.uniqueResultOptional(), Optional.class);
	}

	// U-Update
	public <T> void update(T entity) {
		transactionWrapper.performTransaction(session->session.update(entity));
	}

	// D-Delete
	public <T> void delete(T entity) {
		transactionWrapper.performTransaction(session->session.delete(entity));
	}

	// Other methods
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
		return 0;
	}
}
