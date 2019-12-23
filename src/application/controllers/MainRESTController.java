package application.controllers;

import java.util.LinkedHashMap;
import java.util.List;

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
import application.services.exceptions.ConstraintException;
import application.services.exceptions.NoSuchRecord;
import application.services.exceptions.TableNameRequestBodyException;
import application.services.exceptions.WrongTableNameException;

@RestController
@RequestMapping(value = "/mainPage")
public class MainRESTController {
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
	public <T> T findbyId(@PathVariable String tableName, @PathVariable int id) {
		return daoService.findById(tableName, id);
	}

	@PutMapping(value = { "/{tableName}/{id}" })
	public ResponseEntity<ResponseDTO> update(@PathVariable String tableName, @PathVariable int id,
			@RequestBody LinkedHashMap<String, Object> body) {
		return new ResponseEntity<>(daoService.update(tableName, body), HttpStatus.OK);
	}

	@PostMapping(value = { "/{tableName}" })
	public ResponseEntity<ResponseDTO> save(@PathVariable String tableName,
			@RequestBody LinkedHashMap<String, Object> body) {
		return new ResponseEntity<>(daoService.save(tableName, body), HttpStatus.CREATED);
	}

	// Exception Handlers

	@ExceptionHandler(WrongTableNameException.class)
	public ResponseEntity<ErrorHandler> wrongTableName(WrongTableNameException e) {
		return new ResponseEntity<>(e.getErrorHandler(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TableNameRequestBodyException.class)
	public ResponseEntity<ErrorHandler> tableNameRequestBody(TableNameRequestBodyException e) {
		return new ResponseEntity<>(e.getErrorHandler(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintException.class)
	public ResponseEntity<ErrorHandler> constraintViolated(ConstraintException e) {
		return new ResponseEntity<>(e.getErrorHandler(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(NoSuchRecord.class)
	public ResponseEntity<ErrorHandler> noRecordWithGivenId(NoSuchRecord e) {
		return new ResponseEntity<>(e.getErrorHandler(), HttpStatus.NOT_FOUND);
	}
}
