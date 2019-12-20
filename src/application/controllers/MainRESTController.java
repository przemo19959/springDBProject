package application.controllers;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.services.DaoService;
import application.services.exceptions.TableNameRequestBodyException;
import application.services.exceptions.WrongTableNameException;

@RestController
@RequestMapping(value = "/mainPage")
public class MainRESTController {
	public static final String CONSTRAINT_VIOLITION_MESSAGE="Entity from request body violates unique constraints!";
	private DaoService daoService;

	@Autowired
	public MainRESTController(DaoService daoService) {
		this.daoService = daoService;
	}

	// Request Handlers

	@GetMapping(value = { "/{tableName}" })
	public <T> List<T> findAll(@PathVariable String tableName) {
		return daoService.findAll(tableName);
	}

	@GetMapping(value = { "/{tableName}/{id}" })
	public <T> ResponseEntity<T> findbyId(@PathVariable String tableName, @PathVariable int id) {
		return daoService.findById(tableName, id);
	}

	@PutMapping(value = { "/{tableName}/{id}" })
	public void update(@PathVariable String tableName, @RequestBody Object body, @PathVariable int id) {
		daoService.update(tableName, body);
	}

	@PostMapping(value = { "/{tableName}" })
	public <T> void save(@PathVariable String tableName, @RequestBody T entity) {
		daoService.save(entity);
	}

	// Exception Handlers

	@ExceptionHandler(WrongTableNameException.class)
	public ResponseEntity<String> wrongTableName(WrongTableNameException e) {
		return new ResponseEntity<String>(e.getResponseMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(TableNameRequestBodyException.class)
	public ResponseEntity<String> tableNameRequestBody(TableNameRequestBodyException e) {
		return new ResponseEntity<String>(e.getResponseMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> constraintViolated() {
		return new ResponseEntity<String>(CONSTRAINT_VIOLITION_MESSAGE, HttpStatus.FORBIDDEN);
	}
}
