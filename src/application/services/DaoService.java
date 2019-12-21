package application.services;

import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.OptimisticLockException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import application.dao.Dao;
import application.services.exceptions.ConstraintException;
import application.services.exceptions.NoSuchRecord;
import application.services.exceptions.TableNameRequestBodyException;
import application.services.exceptions.WrongTableNameException;

@Service
public class DaoService {
	private static final String ENTITY_PACKET = "application.entities.";
	@Autowired
	private Dao dao;

	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(String tableName) {
		Class<?> entityClass = getClassFromTableName(tableName);
		return (List<T>) dao.findAll(entityClass);
	}

	@SuppressWarnings("unchecked")
	public <T> T findById(String tableName, int id) {
		Class<?> entityClass = getClassFromTableName(tableName);
		T result=null;
		try {
			result = (T) dao.findById(id, entityClass).get();
		} catch (NoSuchElementException e) {
			throw new NoSuchRecord(e, tableName, id);
		}
		return result;
	}

	public void update(String tableName, Object body) {
		Class<?> entityClass = getClassFromTableName(tableName);
		ObjectMapper mapper = new ObjectMapper();
		try {
			dao.update(mapper.convertValue(body, entityClass)); // dodatkowe mapowanie, bo otrzymuje LinkedListMap
		} catch (Exception e) {
			if (e.getCause() instanceof ConstraintViolationException)
				throw new ConstraintException(e, tableName);
			else if (e instanceof OptimisticLockException)
				throw new TableNameRequestBodyException(e, tableName, body.toString());
			e.printStackTrace();
		}
	}

	public <T> void save(T entity) {
		dao.save(entity);
	}

	// helper methods
	private Class<?> getClassFromTableName(String tableName) {
		try {
			return Class.forName(ENTITY_PACKET + tableName);
		} catch (ClassNotFoundException e) {
			throw new WrongTableNameException(e, tableName);
		}
	}
}
