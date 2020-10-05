package pl.dabrowski.GameShop.exceptions;

import java.text.MessageFormat;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import lombok.Getter;

@Getter
public class ExceptionDTO {
	private static final String DEFAULT_MESSAGE = "default.message";
	private static final String DEFAULT_SOLUTIONS = "default.solutions";

	private static final String SEPARATOR = ":::";
	private static final String SOLUTIONS = "{0}.solutions";
	private static final String MESSAGE = "{0}.message";

	private final String exceptionName;
	private final String message;
	private final String[] solutions;

	private ExceptionDTO(String exceptionName, String message, String[] solutions) {
		this.exceptionName = exceptionName;
		this.message = message;
		this.solutions = solutions;
	}

	public static ExceptionDTO create(Class<? extends RuntimeException> ex, Environment env) {
		String exceptionName = ex.getSimpleName();
		String message = env.containsProperty(MessageFormat.format(MESSAGE, exceptionName))
				? env.getProperty(MessageFormat.format(MESSAGE, exceptionName))
				: env.getProperty(DEFAULT_MESSAGE);
		String solutions = env.containsProperty(MessageFormat.format(SOLUTIONS, exceptionName))
				? env.getProperty(MessageFormat.format(SOLUTIONS, exceptionName))
				: env.getProperty(DEFAULT_SOLUTIONS);
		return new ExceptionDTO(exceptionName, message, solutions.split(SEPARATOR));
	}

	public static ExceptionDTO create(MethodArgumentNotValidException ex) {
		return new ExceptionDTO(MethodArgumentNotValidException.class.getSimpleName(),
				ex.getBindingResult().getAllErrors().stream()
						.map(e -> ((FieldError) e).getField() + ": " + e.getDefaultMessage())
						.collect(Collectors.joining("\n")),
				new String[] {});
	}
}
