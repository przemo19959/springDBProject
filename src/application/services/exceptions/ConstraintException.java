package application.services.exceptions;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

import application.controllers.ErrorHandler;
import application.services.MyException;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class ConstraintException extends MyException {
	public static final String ERROR_MESSAGE = "{0}: Table {1} constraint was broken!";
	public static final String[] SOLUTIONS = {"Check if updated value doesn't apear in another record!"};
	private final ErrorHandler errorHandler;

	public ConstraintException(Exception originalException, String tableName) {
		super(originalException);
		String solutions=Arrays.stream(SOLUTIONS).collect(Collectors.joining(", "));
		errorHandler=new ErrorHandler(MessageFormat.format(ERROR_MESSAGE,
				super.getOriginalException().getCause().getClass().getSimpleName(),//
				tableName),solutions);
	}
}
