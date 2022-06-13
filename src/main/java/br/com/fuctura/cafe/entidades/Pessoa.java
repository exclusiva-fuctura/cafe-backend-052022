package br.com.fuctura.cafe.entidades;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.fuctura.cafe.entidades.dtos.PessoaDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="pessoas")
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="cpf") 
	private String cpf;
	
	@Column()
	private String nome;
			
	@OneToMany(mappedBy="pessoa", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Comida> comidas;
	
	public PessoaDto toDto() {
		return new PessoaDto(this);
	}
	
	public Pessoa(PessoaDto dto) {
		this.cpf = dto.getCpf();
		this.nome = dto.getNome();
		if (null != dto.getItens()) {
			this.comidas = dto.getItens().stream().map(item -> new Comida(item))
					.collect(Collectors.toList());		
		}
	}
}


