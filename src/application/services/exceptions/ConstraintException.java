package application.services.exceptions;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

import application.controllers.ErrorHandler;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class ConstraintException extends RuntimeException {
	public static final String ERROR_MESSAGE = ConstraintException.class.getSimpleName()+": Table {0} constraint was broken!";
	public static final String[] SOLUTIONS = {"Check if updated or saved value doesn't apear in another record!"};
	private final ErrorHandler errorHandler;

	public ConstraintException(String tableName) {
		String solutions=Arrays.stream(SOLUTIONS).collect(Collectors.joining(", "));
		errorHandler=new ErrorHandler(MessageFormat.format(ERROR_MESSAGE,//
				tableName),solutions);
	}
}
