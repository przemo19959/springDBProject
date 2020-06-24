package application.services.exceptions;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

import application.controllers.ErrorHandler;
import application.services.MyException;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class NoSuchRecord extends MyException {
	public static final String ERROR_MESSAGE = "{0}: Table {1} doesn''t contain record with id value {2}!";
	public static final String[] SOLUTIONS = {"Enter another id!"};
	private final ErrorHandler errorHandler;

	public NoSuchRecord(Exception originalException, String tableName, int id) {
		super(originalException);
		String solutions=Arrays.stream(SOLUTIONS).collect(Collectors.joining(", "));
		errorHandler=new ErrorHandler(MessageFormat.format(ERROR_MESSAGE,
				super.getOriginalException().getClass().getSimpleName(),//
				tableName,id),solutions);
	}
}
