package application.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.OptimisticLockException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import application.dao.Dao;
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

	public <T> ResponseEntity<T> findById(String tableName, int id) {
		Class<?> entityClass = getClassFromTableName(tableName);
		@SuppressWarnings("unchecked")
		Optional<T> result = (Optional<T>) dao.findById(id, entityClass);
		if (result.isPresent())
			return ResponseEntity.ok(result.get());
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	public void update(String tableName, Object body) {
		Class<?> entityClass = getClassFromTableName(tableName);
		ObjectMapper mapper = new ObjectMapper();
		try {
			dao.update(mapper.convertValue(body, entityClass)); // dodatkowe mapowanie, bo otrzymuje LinkedListMap
		} catch (OptimisticLockException e) {
			throw new TableNameRequestBodyException(e, tableName, body.toString());
		}
	}

	public <T> void save(T entity) {
		dao.save(entity);
	}

	private Class<?> getClassFromTableName(String tableName) {
		try {
			return Class.forName(ENTITY_PACKET + tableName);
		} catch (ClassNotFoundException e) {
			throw new WrongTableNameException(e, tableName);
		}
	}
}
