package br.com.fuctura.cafe.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fuctura.cafe.entidades.Pessoa;

@Transactional(readOnly = true)
@Repository
public interface PessoaRepositorio extends JpaRepository<Pessoa, String>{
	
	@Query( value = "SELECT * FROM PESSOAS p", nativeQuery = true)
	List<Pessoa> findAll();
	
	@Query( value = "SELECT * FROM PESSOAS p WHERE p.cpf = :cpf", nativeQuery = true)
	Optional<Pessoa> findId(@Param("cpf") String cpf);
	
	@Modifying
	@Transactional(readOnly=false)
	@Query(value = "INSERT INTO PESSOAS (cpf, nome) VALUES (:cpf, :nome)", nativeQuery = true)
	void insert(@Param("cpf") String cpf, @Param("nome") String nome);
	
	@Modifying
	@Transactional(readOnly=false)
	@Query(value = "UPDATE PESSOAS SET nome=:nome) WHERE cpf = :cpf", nativeQuery = true)
	void update(@Param("cpf") String cpf, @Param("nome") String nome);

	@Modifying
	@Transactional(readOnly=false)
	@Query(value = "DELETE PESSOAS WHERE cpf = :cpf", nativeQuery = true)
	void delete(@Param("cpf") String cpf);

}
