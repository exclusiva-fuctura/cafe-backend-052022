package br.com.fuctura.cafe.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fuctura.cafe.entidades.Comida;
import br.com.fuctura.cafe.entidades.Pessoa;
import br.com.fuctura.cafe.repositorio.PessoaRepositorio;
import br.com.fuctura.cafe.services.exceptions.ComidaExistsException;
import br.com.fuctura.cafe.services.exceptions.PessoaExistsException;
import br.com.fuctura.cafe.services.exceptions.PessoaNotExistsException;

@Service
public class PessoaServices {
	
	@Autowired
	private PessoaRepositorio pessoaRepositorio;
	
	@Autowired
	private ComidaServices comidaService;

	public void save(Pessoa pessoa) throws PessoaExistsException, ComidaExistsException {

		// verificar se existe colaborador na base de dados cadastrado
		Optional<Pessoa> objPessoa = this.pessoaRepositorio.findId(pessoa.getCpf());		
		if (objPessoa.isPresent()) {
			throw new PessoaExistsException("Colaborador já cadastrado!");
		}
		

		// cadastrar o colaborador
		this.pessoaRepositorio.insert(pessoa.getCpf(), pessoa.getNome());

		// verifica se existe algum item informado
		if(null != pessoa.getComidas()) {
			// verificar se existe algum item já cadastrado
			for (Comida comida : pessoa.getComidas()) {
				Optional<Comida> objComida = this.comidaService.findByDescricao(comida.getDescricao());
				if (objComida.isPresent()) {
					throw new ComidaExistsException("Item '" + 
							objComida.get().getDescricao() + "' já foi cadastrado por outro colaborador" );	
				}
			}
			// cadastrar a(s) comida(s)
			for (Comida comida : pessoa.getComidas()) {
				comida.setPessoa(pessoa);
				this.comidaService.insert(comida);
			}
		}		
		
		
	}
	
	public void update(Pessoa pessoa) throws PessoaNotExistsException, ComidaExistsException {
		// verificar se existe colaborador na base de dados cadastrado
		Optional<Pessoa> objPessoa = this.pessoaRepositorio.findId(pessoa.getCpf());		
		if (objPessoa.isEmpty()) {
			throw new PessoaNotExistsException("Colaborador não cadastrado!");
		}
		
		// atualizar o colaborador
		if (!objPessoa.get().getCpf().equals(pessoa.getCpf())) {
			this.pessoaRepositorio.update(pessoa.getCpf(), pessoa.getNome());			
		}
		
		// verificar se existe algum item já cadastrado
		if (null != pessoa.getComidas()) {			
			for (Comida comida : pessoa.getComidas()) {
				Optional<Comida> objComida = this.comidaService.findByDescricao(comida.getDescricao());
				if (objComida.isPresent()) {
					throw new ComidaExistsException("Item '" + 
							objComida.get().getDescricao() + "' já foi cadastrado por outro colaborador" );	
				}
			}
			
			// atualizar a(s) comida(s)
			for (Comida comida : pessoa.getComidas()) {
				comida.setPessoa(pessoa);
				this.comidaService.insert(comida);
			}
		}
	}
	
	public void delete(String cpf) throws PessoaNotExistsException {
		Optional<Pessoa> objPessoa = this.pessoaRepositorio.findId(cpf);
		if (objPessoa.isEmpty()) {
			throw new PessoaNotExistsException("Colaborador não cadastrado!");
		}
		
		// remover pessoa
		this.pessoaRepositorio.delete(cpf);
	}
	
	public List<Pessoa> findAll() {
		return this.pessoaRepositorio.findAll();
	}
	
	public Optional<Pessoa> findByCpf(String cpf) {
		return this.pessoaRepositorio.findId(cpf);
	}
	
}
