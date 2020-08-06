package application.services.exceptions;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

import application.controllers.ErrorHandler;
import application.services.MyException;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class WrongTableNameException extends MyException {
	public static final String ERROR_MESSAGE = "{0}: Table {1} doesn''t exist in database!";
	public static final String[] SOLUTIONS = {"Check if table with given name exists in DB!"};
	private final ErrorHandler errorHandler;

	public WrongTableNameException(Exception originalException, String tableName) {
		super(originalException);
		String solutions=Arrays.stream(SOLUTIONS).collect(Collectors.joining(", "));
		errorHandler=new ErrorHandler(MessageFormat.format(ERROR_MESSAGE, super.getOriginalException()//
				.getClass().getSimpleName(),tableName),solutions);
	}
}
