package pl.dabrowski.GameShop.exceptions;

import java.util.NoSuchElementException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
	@Autowired
	private Environment env;

	@ExceptionHandler(value = NoSuchElementException.class)
	public ResponseEntity<ExceptionDTO> handleNoSuchElementException() {
		return new ResponseEntity<ExceptionDTO>(ExceptionDTO.create(NoSuchElementException.class, env),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = ConstraintViolationException.class)
	public ResponseEntity<ExceptionDTO> handleConstraintViolationException() {
		return new ResponseEntity<ExceptionDTO>(ExceptionDTO.create(ConstraintViolationException.class, env),
				HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
		return ResponseEntity.badRequest().body(ExceptionDTO.create(ex));
	}
}
