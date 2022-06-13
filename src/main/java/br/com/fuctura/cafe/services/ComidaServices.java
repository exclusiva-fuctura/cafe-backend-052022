package br.com.fuctura.cafe.services;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fuctura.cafe.entidades.Comida;
import br.com.fuctura.cafe.entidades.Pessoa;
import br.com.fuctura.cafe.repositorio.ComidaRepositorio;

@Service
public class ComidaServices {
	
	@Autowired
	private ComidaRepositorio repository;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	public Optional<Comida> findById(Long id) {
		if (null == id) { return Optional.empty(); }
		return this.repository.findById(id);
	}

	public Optional<Comida> findByDescricao(String descricao) {
		if (null == descricao || descricao.isEmpty() || descricao.isBlank()) { return Optional.empty(); }
		return this.repository.findByDescricao(descricao);
	}

	public Optional<Comida> findByPessoa(Pessoa pessoa) {
		if (null == pessoa) { return Optional.empty(); }
		return this.repository.findByCpf(pessoa.getCpf());
	}
	public void insert(Comida comida) {
		this.repository.insert(comida, this.entityManager);
	}
	
	public void update(Comida comida) {
		this.repository.update(comida, this.entityManager);
	}
	
	public void delete(long id) {
		this.repository.delete(id, this.entityManager);
	}

}
