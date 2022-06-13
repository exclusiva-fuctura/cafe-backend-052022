package br.com.fuctura.cafe.entidades;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.fuctura.cafe.entidades.dtos.ComidaDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comidas")
public class Comida implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String descricao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pessoa_id", nullable = false)
	private Pessoa pessoa;
	
	public Comida(ComidaDto comidaDto) {
		this.id = comidaDto.getId();
		this.descricao = comidaDto.getDescricao();
	}
	
	public ComidaDto toDto() {
		return new ComidaDto(this);  
	}

}
