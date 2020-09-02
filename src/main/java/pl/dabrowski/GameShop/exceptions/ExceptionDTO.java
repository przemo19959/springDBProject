package pl.dabrowski.GameShop.exceptions;

import java.text.MessageFormat;

import org.springframework.core.env.Environment;

import lombok.Getter;

@Getter
public class ExceptionDTO {
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
		return new ExceptionDTO(exceptionName, //
				env.getProperty(MessageFormat.format(MESSAGE, exceptionName)), //
				env.getProperty(MessageFormat.format(SOLUTIONS, exceptionName)).split(","));
	}
}
