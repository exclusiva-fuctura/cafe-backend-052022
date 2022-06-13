package br.com.fuctura.cafe.services.exceptions;

public class PessoaExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public PessoaExistsException(String msg) {
		super(msg);
	}
}
