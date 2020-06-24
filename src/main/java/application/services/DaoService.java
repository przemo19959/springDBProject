package application.services;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.OptimisticLockException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import application.controllers.ResponseDTO;
import application.dao.Dao;
import application.services.exceptions.ConstraintException;
import application.services.exceptions.NoSuchRecord;
import application.services.exceptions.TableNameRequestBodyException;
import application.services.exceptions.WrongTableNameException;

@Service
public class DaoService {
	public static final String SAVE_RESPONSE = "Record was successfully created in table {0}! Assigned id has value {1}.";
	public static final String UPDATE_RESPONSE = "Record with id {0} was successfully updated in table {1}!";
	public static final String DELETE_RESPONSE = "Record with id {0} was successfully deleted from table {1}!";

	private static final String ENTITY_PACKET = "application.entities.";
	@Autowired
	private Dao dao;

	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(String tableName) {
		Class<?> entityClass = getClassFromTableName(tableName);
		List<T> result=(List<T>) dao.findAll(entityClass);
		if(result.size()==0)
			return (List<T>) dao.getColumnNames(entityClass);
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> T findById(String tableName, int id) {
		Class<?> entityClass = getClassFromTableName(tableName);
		T result = null;
		try {
			result = (T) dao.findById(id, entityClass).get();
		} catch (NoSuchElementException e) {
			throw new NoSuchRecord(e, tableName, id);
		}
		return result;
	}

	public ResponseDTO update(String tableName, LinkedHashMap<String, Object> body) {
		Class<?> entityClass = getClassFromTableName(tableName);
		ObjectMapper mapper = new ObjectMapper();
		try {
			dao.update(mapper.convertValue(body, entityClass)); // dodatkowe mapowanie, bo otrzymuje LinkedListMap
		} catch (Exception e) {
			if (e.getCause() instanceof ConstraintViolationException)
				throw new ConstraintException(tableName);
			else if (e instanceof OptimisticLockException)
				throw new TableNameRequestBodyException(e, tableName, body.toString());
			e.printStackTrace();
		}
		return new ResponseDTO(MessageFormat.format(UPDATE_RESPONSE, body.get("id"), tableName));
	}

	public ResponseDTO save(String tableName, LinkedHashMap<String, Object> body) {
		body.put("id", "0"); // po stronie forntu nie jest ustawiana wartosc, przydziela ja DB
		Class<?> entityClass = getClassFromTableName(tableName);
		ObjectMapper mapper = new ObjectMapper();
		int result = -1;
		try {
			result = dao.save(mapper.convertValue(body, entityClass));
		} catch (Exception e) {
			if (e instanceof ConstraintViolationException)
				throw new ConstraintException(tableName);
//			else if (e instanceof OptimisticLockException)
//				throw new TableNameRequestBodyException(e, tableName, body.toString());
			e.printStackTrace();
		}
		return new ResponseDTO(MessageFormat.format(SAVE_RESPONSE, tableName, result));
	}

	public ResponseDTO deleteById(String tableName, int id) {
		Class<?> entityClass = getClassFromTableName(tableName);
		Object recordToDelete=null;
		try {
			recordToDelete = dao.findById(id, entityClass).get();
		} catch (NoSuchElementException e) {
			throw new NoSuchRecord(e, tableName, id);
		}
		dao.delete(recordToDelete);
		return new ResponseDTO(MessageFormat.format(DELETE_RESPONSE, id,tableName));
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
