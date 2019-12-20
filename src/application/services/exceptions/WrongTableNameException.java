package application.services.exceptions;

import java.text.MessageFormat;

import application.services.MyException;

public class WrongTableNameException extends MyException {
	public static final String WRONG_TABLENAME = "{0}: Table {1} doesn''t exist in database!";
	private final String tableName;

	public WrongTableNameException(Exception originalException, String tableName) {
		super(originalException);
		this.tableName = tableName;
	}

	public String getResponseMessage() {
		return MessageFormat.format(WRONG_TABLENAME, super.getOriginalException()//
				.getClass().getSimpleName(), tableName);
	}
}
