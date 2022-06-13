package br.com.fuctura.cafe.services.exceptions;

public class PessoaNotExistsException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public PessoaNotExistsException(String msg) {
		super(msg);
	}

}
