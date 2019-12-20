package application.services.exceptions;

import java.text.MessageFormat;

import javax.persistence.OptimisticLockException;

import application.services.MyException;

public class TableNameRequestBodyException extends MyException {
	public static final String TABLE_REQUESTBODY_MISMATCH = "{0}: Table {1} doesn''t match request body {2}!";
	private final String tableName;
	private final String requestBody;

	public TableNameRequestBodyException(Exception originalException, String tableName, String requestBody) {
		super(originalException);
		this.tableName = tableName;
		this.requestBody = requestBody;
	}

	public String getResponseMessage() {
		return MessageFormat.format(TABLE_REQUESTBODY_MISMATCH, //
				OptimisticLockException.class.getSimpleName(), tableName, requestBody);
	}
}
