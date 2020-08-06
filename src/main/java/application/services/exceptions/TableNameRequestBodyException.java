package application.services.exceptions;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

import application.controllers.ErrorHandler;
import application.services.MyException;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class TableNameRequestBodyException extends MyException {
	public static final String ERROR_MESSAGE = "{0}: Table {1} doesn''t match request body {2}!";
	public static final String[] SOLUTIONS = {"Check if request body fields matches tableName from request path!"};
	
	private final ErrorHandler errorHandler;

	public TableNameRequestBodyException(Exception originalException, String tableName, String requestBody) {
		super(originalException);
		String solutions=Arrays.stream(SOLUTIONS).collect(Collectors.joining(", "));
		errorHandler=new ErrorHandler(MessageFormat.format(ERROR_MESSAGE, super.getOriginalException()//
				.getClass().getSimpleName(),tableName,requestBody),solutions);
	}
}
