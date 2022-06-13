package br.com.fuctura.cafe.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.fuctura.cafe.entidades.Comida;
import br.com.fuctura.cafe.entidades.Pessoa;
import br.com.fuctura.cafe.services.exceptions.ComidaExistsException;
import br.com.fuctura.cafe.services.exceptions.PessoaExistsException;
import br.com.fuctura.cafe.services.exceptions.PessoaNotExistsException;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PessoaServicesTest {
	
	@Autowired
	private PessoaServices pessoaServices;
	
	private static Pessoa novaPessoa1;
	private static Pessoa novaPessoa2;
	private static Pessoa novaPessoa3;
	private static Pessoa novaPessoa4;
	
	@BeforeAll
	static void setup() {
		novaPessoa1 = new Pessoa();  
		novaPessoa1.setCpf("05734901040");
		novaPessoa1.setNome("FULANO DE TAL");
		
		novaPessoa2 = new Pessoa();  
		novaPessoa2.setCpf("99850955023");
		novaPessoa2.setNome("SICRANO DE TAL");
		
		novaPessoa3 = new Pessoa();  
		novaPessoa3.setCpf("10944747035");
		novaPessoa3.setNome("BELTRANO DE TAL");
		
		novaPessoa4 = new Pessoa();  
		novaPessoa4.setCpf("69328254060");
		novaPessoa4.setNome("SINCERANO DE TAL");
	}
	
	@DisplayName("Gravar uma pessoa sem item informado com sucesso")
	@Order(1)
	@Test
	public void testSaveSemComida_OK() throws PessoaExistsException, ComidaExistsException {
		this.pessoaServices.save(novaPessoa1);
		
		Optional<Pessoa> pessoaBanco = this.pessoaServices.findByCpf(novaPessoa1.getCpf());
		
		// verificar se encontrou a Pessoa no banco
		assertTrue(pessoaBanco.isPresent());
		// verificar se a pessoa do banco é identica a novaPessoa
		assertEquals(novaPessoa1.getCpf(), pessoaBanco.get().getCpf());
		assertEquals(novaPessoa1.getNome(), pessoaBanco.get().getNome());
	}
	
	@DisplayName("Gravar uma pessoa com 1 item novo com sucesso")
	@Order(2)
	@Test
	public void testSaveComUmItem_OK() throws PessoaExistsException, ComidaExistsException {

		// lista de comidas
		List<Comida> itens = new ArrayList<>();
		// criar a comida
		Comida comida = new Comida();
		comida.setDescricao("Coxinhas");
		itens.add(comida);
		novaPessoa2.setComidas(itens);
		// persistir
		this.pessoaServices.save(novaPessoa2);
		// recuperar
		Optional<Pessoa> pessoaBanco = this.pessoaServices.findByCpf(novaPessoa2.getCpf());
		
		// verificar se encontrou a Pessoa no banco
		assertTrue(pessoaBanco.isPresent());
		
		assertEquals(pessoaBanco.get().getComidas().size(), 
				novaPessoa2.getComidas().size());
	}
	
	@DisplayName("Gravar uma pessoa com vários itens novos com sucesso")
	@Order(3)
	@Test
	public void testSaveComVariosItem_OK() throws PessoaExistsException, ComidaExistsException {
		// lista de comidas
		List<Comida> itens = new ArrayList<>();
		// criar a comida
		Comida item1 = new Comida();
		item1.setDescricao("Brigadeiro");
		itens.add(item1);
		Comida item2 = new Comida();
		item2.setDescricao("Pasteis");
		itens.add(item2);
		novaPessoa3.setComidas(itens);
		// persistir
		this.pessoaServices.save(novaPessoa3);
		// recuperar
		Optional<Pessoa> pessoaBanco = this.pessoaServices.findByCpf(novaPessoa3.getCpf());
		
		// verificar se encontrou a Pessoa no banco
		assertTrue(pessoaBanco.isPresent());
		
		assertEquals(pessoaBanco.get().getComidas().size(), 
				novaPessoa3.getComidas().size());	
	}
	
	@DisplayName("Gravar uma pessoa com 1 item existente com falha")
	@Order(4)
	@Test
	public void testSaveComUmItem_Falha() {
		
		// lista de comidas
		List<Comida> itens = new ArrayList<>();
		// criar a comida
		Comida comida = new Comida();
		comida.setDescricao("Coxinhas");
		itens.add(comida);
		novaPessoa4.setComidas(itens);
		// persistir
		Exception exception = assertThrows(ComidaExistsException.class, () -> {
			this.pessoaServices.save(novaPessoa4);
	    });
				
		assertTrue(exception.getMessage()
				.contains("já foi cadastrado por outro colaborador"));
	}
	
	@DisplayName("Gravar uma pessoa existente com falha")
	@Order(5)
	@Test
	public void testSaveColaboradorExistente_Falha() {
				
		// persistir
		Exception exception = assertThrows(PessoaExistsException.class, () -> {
			this.pessoaServices.save(novaPessoa1);
	    });
				
		assertTrue(exception.getMessage()
				.contains("Colaborador já cadastrado"));
	}
	
	@DisplayName("Alterar uma pessoa existente com sucesso")
	@Order(6)
	@Test
	public void testUpdateColaboradorExistente_OK() throws PessoaNotExistsException, ComidaExistsException {

		novaPessoa1.setNome("Novo nome");
		// persistir
		this.pessoaServices.update(novaPessoa1);
	    				
		// recuperar
		Optional<Pessoa> pessoaBanco = this.pessoaServices.findByCpf(novaPessoa1.getCpf());
		
		// verificar se encontrou a Pessoa no banco
		assertTrue(pessoaBanco.isPresent());
		
		assertTrue(pessoaBanco.get().getCpf().equals(novaPessoa1.getCpf()));	
		
	}
	
	@DisplayName("Alterar uma pessoa não existente com falha")
	@Order(7)
	@Test
	public void testUpdateColaboradorExistente_Falha() {
		String cpf = novaPessoa1.getCpf().substring(0,10);
		novaPessoa1.setCpf(cpf);		
		// persistir
		Exception exception = assertThrows(PessoaNotExistsException.class, () -> {
			this.pessoaServices.update(novaPessoa1);
	    });
				
		assertTrue(exception.getMessage()
				.contains("Colaborador não cadastrado"));
	}
	
	@DisplayName("Alterar uma pessoa com 1 item existente com falha")
	@Order(8)
	@Test
	public void testUpdateComUmItem_Falha() {
		
		// lista de comidas
		List<Comida> itens = new ArrayList<>();
		// criar a comida
		Comida comida = new Comida();
		comida.setDescricao("Coxinhas");
		itens.add(comida);
		novaPessoa2.setComidas(itens);
		// persistir
		Exception exception = assertThrows(ComidaExistsException.class, () -> {
			this.pessoaServices.update(novaPessoa2);
	    });
				
		assertTrue(exception.getMessage()
				.contains("já foi cadastrado por outro colaborador"));
	}
	
	@DisplayName("Delete uma pessoa existente com sucesso")
	@Order(9)
	@Test
	public void testDeleteComUmItem_OK() throws PessoaNotExistsException {
		String cpf = "73400338079";
		// ANTES: recuperar
		Optional<Pessoa> pessoaBancoAntes = this.pessoaServices
				.findByCpf(cpf);
		// checar se existe
		assertTrue(pessoaBancoAntes.isPresent());
		
		// remover pessoa
		this.pessoaServices.delete(cpf);
	    
		// DEPOIS: verificar
		Optional<Pessoa> pessoaBancoDepois = this.pessoaServices
				.findByCpf(cpf);
		// checar que não existe
		assertTrue(pessoaBancoDepois.isEmpty());
	}
	
	@DisplayName("Delete uma pessoa inexistente com falha")
	@Order(10)
	@Test
	public void testDeleteComUmItem_Falha() {
		String cpf = "73400338078";
		
		// remover pessoa
		Exception exception = assertThrows(PessoaNotExistsException.class, () -> {
			this.pessoaServices.delete(cpf);
	    });
		
		assertTrue(exception.getMessage()
				.contains("Colaborador não cadastrado"));
	}
}
