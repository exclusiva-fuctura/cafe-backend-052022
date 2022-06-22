package br.com.fuctura.cafe.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fuctura.cafe.config.SwaggerConfig;
import br.com.fuctura.cafe.entidades.Comida;
import br.com.fuctura.cafe.entidades.Pessoa;
import br.com.fuctura.cafe.entidades.dtos.PessoaDto;
import br.com.fuctura.cafe.services.PessoaServices;
import br.com.fuctura.cafe.services.exceptions.ComidaExistsException;
import br.com.fuctura.cafe.services.exceptions.PessoaExistsException;
import br.com.fuctura.cafe.services.exceptions.PessoaNotExistsException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = {SwaggerConfig.PESSOA_TAG})
@RestController
@RequestMapping("/api")
public class PessoaController {

	@Autowired
	private PessoaServices servico;
		
	@ApiOperation(value="Listagem de todas as pessoas cadastradas")
	@GetMapping(value="/pessoa", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<PessoaDto>> index() {
		
		// iniciar a listagem
		List<PessoaDto> pessoasDto = new ArrayList<>();
		
		// obter a listagem de todas as pessoas
		List<Pessoa> pessoas = this.servico.findAll();
		if (!pessoas.isEmpty()) {
			pessoasDto = pessoas.stream()
					.map( pessoa -> pessoa.toDto())
					.collect(Collectors.toList());
		}		
		
		return ResponseEntity.status(HttpStatus.OK).body(pessoasDto); 
	}
	
	@ApiOperation(value="Busca o colaborador cadastrado")
	@GetMapping(value="/pessoa/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<PessoaDto> show(@PathVariable String cpf) {
						
		Optional<Pessoa> pessoa = this.servico.findByCpf(cpf);
		if (pessoa.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(pessoa.get().toDto());	
		}
		
		PessoaDto pessoaDto = new PessoaDto();
		pessoaDto.setCpf(cpf);
		pessoaDto.setMensagem("Colaborador não encontrado!");
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pessoaDto); 
	}
	
	@ApiOperation(value="Cadastrar Colaborador")
	@PostMapping(value="/pessoa", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<PessoaDto> store(@RequestBody PessoaDto dto) {
		
		// verificar se o cpf esta preenchido
		if (null == dto.getCpf() || dto.getCpf().isEmpty()) {
			dto.setMensagem("CPF do colaborador não informado!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
		}
		
		// verificar se o nome esta preenchido
		if (null == dto.getNome() || dto.getNome().equals("")) {
			dto.setMensagem("Nome do colaborador não informado!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);	
		}
		
		// Persistir a pessoa
		Pessoa pessoa = new Pessoa(dto);
		try {
			this.servico.save(pessoa);
			
			// recuperar a pessoa cadastrada
			Optional<Pessoa> pessoaBanco = this.servico.findByCpf(dto.getCpf());
			if (pessoaBanco.isPresent()) {
				PessoaDto pessoaDto = pessoaBanco.get().toDto();
				dto = pessoaDto;
			}
			
		} catch (PessoaExistsException e) {
			dto.setMensagem(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);	
		} catch (ComidaExistsException e) {
			dto.setMensagem(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);	
		}		
		
		dto.setMensagem("Colaborador cadastrado com sucesso!");
		return ResponseEntity.status(HttpStatus.CREATED).body(dto); 
	}
	
	@ApiOperation(value="Atualizar Colaborador")
	@PutMapping(value="/pessoa/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<PessoaDto> update(@PathVariable String cpf, @RequestBody PessoaDto dto) {
		
		// verificar se o cpf da URL é um numero
		if (cpf.isBlank() || !NumberUtils.isParsable(cpf)) {
			dto.setMensagem("CPF do colaborador inválido na url!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
		}
		
		// verificar se o cpf esta preenchido
		if (null == dto.getCpf() || dto.getCpf().isEmpty()) {
			dto.setMensagem("CPF do colaborador não informado!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
		}
		
		// verificar se o nome esta preenchido
		if (null == dto.getNome() || dto.getNome().equals("")) {
			dto.setMensagem("Nome do colaborador não informado!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);	
		}
		
		// verificar se o CPF da URl é o mesmo do body
		if (!cpf.equals(dto.getCpf())) {
			dto.setMensagem("Cpf do colaborador da url e do dto estão incompatíveis!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);		
		}
		
		// Persistir a pessoa
		Pessoa pessoa = new Pessoa(dto);
		try {
			// atualizar as comidas		
			List<Comida> comidas = dto.getItens().stream()
					.map(item -> new Comida(item)).collect(Collectors.toList());
			pessoa.setComidas(comidas);
			// persistir
			this.servico.update(pessoa);
			
			// recuperar a pessoa cadastrada
			Optional<Pessoa> pessoaBanco = this.servico.findByCpf(dto.getCpf());
			if (pessoaBanco.isPresent()) {
				PessoaDto pessoaDto = pessoaBanco.get().toDto();
				dto = pessoaDto;
			}
			
		} catch (PessoaNotExistsException e) {
			dto.setMensagem(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);	
		} catch (ComidaExistsException e) {
			dto.setMensagem(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);	
		}		
		
		dto.setMensagem("Colaborador atualizado com sucesso!");
		return ResponseEntity.status(HttpStatus.OK).body(dto); 
	}
	
	@ApiOperation(value="Excluir Colaborador")
	@DeleteMapping(value="/pessoa/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<PessoaDto> delete(@PathVariable String cpf) {
		// iniciar o dto
		PessoaDto dto = new PessoaDto();		
		
		// verificar se o cpf da URL é um numero
		if (cpf.isBlank() || !NumberUtils.isParsable(cpf)) {
			dto.setMensagem("CPF do colaborador inválido na url!");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
		}
		
		// atribuir o CPF no dto
		dto.setCpf(cpf);
		
		// remover pessoa
		try {
			this.servico.delete(cpf);
			dto.setMensagem("Colaborador excluido com sucesso!");
			return ResponseEntity.status(HttpStatus.OK).body(dto);
			
		} catch (PessoaNotExistsException e) {
			dto.setMensagem(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);	
		}				
	}
		
}
