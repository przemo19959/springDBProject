package application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import application.dao.Dao;
import lombok.SneakyThrows;

@Service
public class DaoService {
	private static final String ENTITY_PACKET="application.entities.";
	
	@Autowired
	private Dao dao;
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(String tableName) {
		Class<?> entityClass=getClassFromTableName(tableName);
		return (List<T>) dao.findAll(entityClass);
	}
	
	public <T> ResponseEntity<T> findById(String tableName,int id) {
		Class<?> entityClass=getClassFromTableName(tableName);
		@SuppressWarnings("unchecked")
		Optional<T> result=(Optional<T>) dao.findById(id, entityClass);
		if(result.isPresent())
			return ResponseEntity.ok(result.get());
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@SneakyThrows(ClassNotFoundException.class)
	private Class<?> getClassFromTableName(String tableName){
		return Class.forName(ENTITY_PACKET+tableName);
	}
}
