package application.controllers;

import lombok.Data;

@Data
public class ErrorHandler {
	private final String errorMessage; 
	private final String solutions;
}
