package br.com.fuctura.cafe.entidades.dtos;

import br.com.fuctura.cafe.entidades.Comida;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ComidaDto {

	private long id;
	
	private String descricao;
	
	public ComidaDto(Comida comida) {
		this.id = comida.getId();
		this.descricao = comida.getDescricao();
	}
	
}
