package application.services;

import lombok.Getter;

@Getter
public class MyException extends RuntimeException {
	private final Exception originalException;
	
	public MyException(Exception originalException) {
		this.originalException = originalException;
	}
}
