package br.com.fuctura.cafe.repositorio;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fuctura.cafe.entidades.Comida;

@Transactional(readOnly = true)
@Repository
public interface ComidaRepositorio extends JpaRepository<Comida, Long> {

	@Query(value = "SELECT * FROM COMIDAS c", nativeQuery = true)
	List<Comida> findAll();

	@Query(value = "SELECT * FROM COMIDAS c WHERE c.id = :id", nativeQuery = true)
	Optional<Comida> findId(@Param("id") long id);
	
	@Query(value = "SELECT * FROM COMIDAS c WHERE c.descricao = :nome", nativeQuery = true)
	Optional<Comida> findByDescricao(@Param("nome") String descricao);

	@Query(value = "SELECT * FROM COMIDAS c WHERE c.pessoa_id = :cpf", nativeQuery = true)
	Optional<Comida> findByCpf(@Param("cpf") String cpf);

	@Query(value = "SELECT * FROM COMIDAS c WHERE c.pessoa_id = :cpf", nativeQuery = true)
	Optional<Comida> findOne(@Param("cpf") String cpf);
	
	@Modifying
	@Transactional
	default void insert(Comida comida, EntityManager entityManager) {
		entityManager.createNativeQuery("INSERT INTO comidas (descricao, pessoa_id) VALUES (?,?)")
		.setParameter(1, comida.getDescricao())
		.setParameter(2, comida.getPessoa().getCpf())
		.executeUpdate();
	}

	@Transactional
	default void update(Comida comida, EntityManager entityManager) {
		entityManager.createNativeQuery("UPDATE comidas c SET descricao=?, pessoa_id=? WHERE id=?")
		.setParameter(1, comida.getDescricao())
		.setParameter(2, comida.getPessoa().getCpf())
		.setParameter(3, comida.getId())
		.executeUpdate();
	}
	
	@Transactional
	default void delete(Long id, EntityManager entityManager) {
		entityManager.createNativeQuery("DELETE from comidas WHERE id=?")
		.setParameter(1, id)
		.executeUpdate();
	}
	
}
