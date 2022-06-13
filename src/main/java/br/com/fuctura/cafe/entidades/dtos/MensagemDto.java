package br.com.fuctura.cafe.entidades.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MensagemDto {

	private int status;
	
	private String mensagem;
	
	private String caminho;
	
	public MensagemDto(int status, String mensagem) {
		this.status = status;
		this.mensagem = mensagem;
	}
}
