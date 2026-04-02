package me.mato.app.exception;

public class UnauthorizedException extends RuntimeException {
	public UnauthorizedException() {
		super("Unauthorized: Please log in to perform this action");
	}
}
