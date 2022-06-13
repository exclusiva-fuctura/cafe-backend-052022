package br.com.fuctura.cafe.entidades.dtos;

import java.util.List;
import java.util.stream.Collectors;

import br.com.fuctura.cafe.entidades.Pessoa;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PessoaDto {

	private String cpf;
	
	private String nome;
	
	private List<ComidaDto> itens;
	
	private String mensagem;
	
	public PessoaDto(Pessoa pessoa) {
		this.cpf = pessoa.getCpf();
		this.nome = pessoa.getNome();
		this.itens = pessoa.getComidas().stream()
				.map( comida -> comida.toDto())
				.collect(Collectors.toList());
	}
		
}
