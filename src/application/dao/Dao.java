package application.dao;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.SneakyThrows;

@Repository
@Transactional
public class Dao {
	public static final String FIND_ALL = "SELECT * FROM {0};";
	public static final String FIND_BY_ID = "SELECT * FROM {0} WHERE id= {1};";

	@Autowired
	private SessionFactory sessionFactory;

	// CRUD operations
	// C-Create
	public <T> int save(T entity) {
		sessionFactory.getCurrentSession().save(entity);
		return getPrimaryKeyValue(entity);
	}

	// R-Retrieve
	public <T> List<T> findAll(Class<T> resultType) {
		return sessionFactory.getCurrentSession()
				.createNativeQuery(MessageFormat.format(FIND_ALL, getTableName(resultType)), resultType).list();
	}

	public <T> Optional<T> findById(int id, Class<T> resultType) {
		return sessionFactory.getCurrentSession()
				.createNativeQuery(MessageFormat.format(FIND_BY_ID, getTableName(resultType), id), resultType)
				.uniqueResultOptional();
	}

	// U-Update
	public <T> void update(T entity) {
		sessionFactory.getCurrentSession().update(entity);
	}

	// D-Delete
	public <T> void delete(T entity) {
		sessionFactory.getCurrentSession().delete(entity);
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
			if (field.isAnnotationPresent(Id.class))
				return field.getInt(entity);
		}
		return 0;
	}
}
