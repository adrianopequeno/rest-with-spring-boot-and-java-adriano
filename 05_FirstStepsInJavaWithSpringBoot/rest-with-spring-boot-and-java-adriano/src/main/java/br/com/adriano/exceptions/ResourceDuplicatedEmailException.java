package br.com.adriano.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceDuplicatedEmailException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ResourceDuplicatedEmailException(String ex) {
		super(ex);
	}

	public ResourceDuplicatedEmailException(String ex, Throwable cause) {
		super(ex, cause);
	}

}
