package com.gx2.digio.gyowanny.compras.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gx2.digio.gyowanny.compras.entity.Compra;
import com.gx2.digio.gyowanny.compras.entity.Cliente;
import com.gx2.digio.gyowanny.compras.entity.Produto;
import com.gx2.digio.gyowanny.compras.repository.ClienteFielProjection;
import com.gx2.digio.gyowanny.compras.repository.ClienteRepository;
import com.gx2.digio.gyowanny.compras.repository.CompraRepository;
import com.gx2.digio.gyowanny.compras.repository.ProdutoRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class CompraServiceImplTest {

  @Mock
  private CompraRepository compraRepository;

  @Mock
  private ClienteRepository clienteRepository;

  @Mock
  private ProdutoRepository produtoRepository;

  @InjectMocks
  private CompraServiceImpl compraService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllComprasSortedByValue() {
    // Create sample Compra objects with different valores
    Compra compra1 = new Compra();
    compra1.setValorTotal(100.0);

    Compra compra2 = new Compra();
    compra2.setValorTotal(50.0);

    // Add the Compra objects to a list
    List<Compra> compras = List.of(compra1, compra2);

    // Configure the mock to return the list sorted by valorTotal in ascending order
    when(compraRepository.findAllByOrderByValorTotalAsc()).thenReturn(
        compras.stream()
            .sorted(Comparator.comparing(Compra::getValorTotal))
            .collect(Collectors.toList())
    );

    // Call the service method
    List<Compra> result = compraService.getAllComprasSortedByValue();

    // Assert that the result list is sorted in ascending order
    assertEquals(2, result.size());
    assertEquals(50.0, result.get(0).getValorTotal());  // Expecting the smallest value first
    assertEquals(100.0, result.get(1).getValorTotal()); // Expecting the larger value second
  }

  @Test
  void testGetMaiorCompraPorAno() {
    int ano = 2024;
    Compra compra = new Compra();
    compra.setValorTotal(200.0);

    when(compraRepository.findFirstByAnoCompraOrderByValorTotalDesc(ano))
        .thenReturn(Optional.of(compra));

    Compra result = compraService.getMaiorCompraPorAno(ano);

    assertNotNull(result);
    assertEquals(200.0, result.getValorTotal());
  }

  @Test
  void testGetMaiorCompraPorAnoThrowsException() {
    int ano = 2024;

    when(compraRepository.findFirstByAnoCompraOrderByValorTotalDesc(ano))
        .thenReturn(Optional.empty());

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      compraService.getMaiorCompraPorAno(ano);
    });

    assertEquals("Nenhuma compra encontrada para o ano: " + ano, thrown.getMessage());
  }

  @Test
  void testGetClientesFieis() {
    Cliente cliente1 = new Cliente();
    Cliente cliente2 = new Cliente();
    ClienteFielProjection projection1 = mock(ClienteFielProjection.class);
    ClienteFielProjection projection2 = mock(ClienteFielProjection.class);

    when(compraRepository.findTop3ClientesFieis(PageRequest.of(0, 3)))
        .thenReturn(List.of(projection1, projection2));

    when(projection1.getCliente()).thenReturn(cliente1);
    when(projection2.getCliente()).thenReturn(cliente2);

    List<Cliente> result = compraService.getClientesFieis();

    assertEquals(2, result.size());
    assertTrue(result.contains(cliente1));
    assertTrue(result.contains(cliente2));
  }

  @Test
  void testGetRecomendacaoDeVinho() {
    Cliente cliente = new Cliente();
    Produto produto = new Produto();
    String tipoVinho = "Tinto";

    when(compraRepository.findTipoVinhoMaisCompradoByCliente(cliente))
        .thenReturn(Optional.of(tipoVinho));

    when(produtoRepository.findFirstByTipo(tipoVinho))
        .thenReturn(Optional.of(produto));

    Produto result = compraService.getRecomendacaoDeVinho(cliente);

    assertNotNull(result);
    assertEquals(produto, result);
  }

  @Test
  void testGetRecomendacaoDeVinhoThrowsExceptionNoTipo() {
    Cliente cliente = new Cliente();

    when(compraRepository.findTipoVinhoMaisCompradoByCliente(cliente))
        .thenReturn(Optional.empty());

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      compraService.getRecomendacaoDeVinho(cliente);
    });

    assertEquals("Nenhum tipo de vinho encontrado para o cliente: " + cliente.getNome(), thrown.getMessage());
  }

  @Test
  void testGetRecomendacaoDeVinhoThrowsExceptionNoProduto() {
    Cliente cliente = new Cliente();
    String tipoVinho = "Tinto";

    when(compraRepository.findTipoVinhoMaisCompradoByCliente(cliente))
        .thenReturn(Optional.of(tipoVinho));

    when(produtoRepository.findFirstByTipo(tipoVinho))
        .thenReturn(Optional.empty());

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      compraService.getRecomendacaoDeVinho(cliente);
    });

    assertEquals("Nenhum produto encontrado para o tipo de vinho: " + tipoVinho, thrown.getMessage());
  }

}