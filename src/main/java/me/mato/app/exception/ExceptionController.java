package me.mato.app.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException e) {
		return ResponseEntity.status(403).body(e.getMessage());
	}

	@ExceptionHandler(TaskNotFoundException.class)
	public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException e) {
		return ResponseEntity.status(404).body(e.getMessage());
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException e) {
		return ResponseEntity.status(401).body(e.getMessage());
	}

}
