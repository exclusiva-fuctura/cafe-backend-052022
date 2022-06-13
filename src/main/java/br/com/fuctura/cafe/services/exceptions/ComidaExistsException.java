package br.com.fuctura.cafe.services.exceptions;

public class ComidaExistsException extends Exception {
		
	private static final long serialVersionUID = 1L;

	public ComidaExistsException(String msg) {
		super(msg);
	}

}
