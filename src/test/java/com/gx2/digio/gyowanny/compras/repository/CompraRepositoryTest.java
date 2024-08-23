package com.gx2.digio.gyowanny.compras.repository;

import com.gx2.digio.gyowanny.compras.entity.Cliente;
import com.gx2.digio.gyowanny.compras.entity.Compra;
import com.gx2.digio.gyowanny.compras.entity.Produto;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = true)
public class CompraRepositoryTest {

  @Autowired
  private CompraRepository compraRepository;

  @Autowired
  private ClienteRepository clienteRepository;

  @Autowired
  private ProdutoRepository produtoRepository;

  private Cliente cliente;
  private Produto produto;
  private Compra compra1;
  private Compra compra2;

  @BeforeEach
  void setUp() {
    cliente = Cliente.builder().nome("Cliente Teste").cpf("12312312399").build();
    clienteRepository.save(cliente);

    produto = Produto.builder().id(1L).tipo("Tinto").anoCompra(2024).preco(24.0).safra("2011").build();
    produtoRepository.save(produto);

    compra1 = Compra.builder().cliente(cliente).produto(produto).quantidade(1).valorTotal(100.0).build();
    compraRepository.save(compra1);

    compra2 = Compra.builder().cliente(cliente).produto(produto).quantidade(1).valorTotal(200.0).build();
    compraRepository.save(compra2);
  }

  @Test
  @DirtiesContext
  void testFindAllByOrderByValorTotalAsc() {
    Compra compra3 = Compra.builder().cliente(cliente).produto(produto).quantidade(1).valorTotal(150.0).build();
    compraRepository.save(compra3);

    List<Compra> result = compraRepository.findAllByOrderByValorTotalAsc();

    assertEquals(3, result.size());
    assertEquals(100.0, result.get(0).getValorTotal());
    assertEquals(150.0, result.get(1).getValorTotal());
    assertEquals(200.0, result.get(2).getValorTotal());
  }

  @Test
  @DirtiesContext
  void testFindFirstByAnoCompraOrderByValorTotalDesc() {
    Optional<Compra> result = compraRepository.findFirstByAnoCompraOrderByValorTotalDesc(2024);

    assertTrue(result.isPresent());
    assertEquals(200.0, result.get().getValorTotal());
  }

  @Test
  @DirtiesContext
  void testFindFirstByAnoCompraOrderByValorTotalDescNoResults() {
    Optional<Compra> result = compraRepository.findFirstByAnoCompraOrderByValorTotalDesc(2025);

    assertFalse(result.isPresent());
  }

  @Test
  @DirtiesContext
  void testFindTipoVinhoMaisCompradoByCliente() {
    Optional<String> result = compraRepository.findTipoVinhoMaisCompradoByCliente(cliente);

    assertTrue(result.isPresent());
    assertEquals("Tinto", result.get());
  }

  @Test
  @DirtiesContext
  void testFindTipoVinhoMaisCompradoByClienteNoResults() {
    Cliente clienteNovo = Cliente.builder().nome("Cliente Novo").cpf("13313312399").build();
    clienteRepository.save(clienteNovo);

    Optional<String> result = compraRepository.findTipoVinhoMaisCompradoByCliente(clienteNovo);

    assertFalse(result.isPresent());
  }

  @Test
  @DirtiesContext
  void testFindTop3ClientesFieis() {
    System.out.println("Count clients: " + clienteRepository.count());
    Cliente cliente2 = Cliente.builder().nome("Cliente 2").cpf("15315315399").build();
    clienteRepository.save(cliente2);

    List<ClienteFielProjection> result = compraRepository.findTop3ClientesFieis(PageRequest.of(0, 3));

    // Verify the size is as expected
    assertEquals(1, result.size());
    // Verify the single result matches the expected client
    assertEquals(cliente, result.get(0).getCliente());
    // Ensure total purchases and total spend are correct
    assertEquals(2, result.get(0).getTotalCompras());
    assertEquals(300.0, result.get(0).getTotalGasto());
  }
}
