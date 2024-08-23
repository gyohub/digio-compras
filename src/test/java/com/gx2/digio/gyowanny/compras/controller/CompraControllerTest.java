package com.gx2.digio.gyowanny.compras.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gx2.digio.gyowanny.compras.entity.Cliente;
import com.gx2.digio.gyowanny.compras.entity.Compra;
import com.gx2.digio.gyowanny.compras.entity.Produto;
import com.gx2.digio.gyowanny.compras.repository.ClienteRepository;
import com.gx2.digio.gyowanny.compras.service.CompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompraController.class)
public class CompraControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CompraService compraService;

  @MockBean
  private ClienteRepository clienteRepository;

  @InjectMocks
  private CompraController compraController;

  @Autowired
  private ObjectMapper objectMapper;

  private Cliente cliente;
  private Produto produto;
  private Compra compra1;
  private Compra compra2;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    cliente = Cliente.builder().nome("Cliente Teste").cpf("12312312300").build();

    produto = Produto.builder().tipo("Tinto").anoCompra(2024).build();

    compra1 = Compra.builder().cliente(cliente).produto(produto).valorTotal(100.0).build();
    compra2 = Compra.builder().cliente(cliente).produto(produto).valorTotal(200.0).build();
  }

  @Test
  void testGetCompras() throws Exception {
    when(compraService.getAllComprasSortedByValue()).thenReturn(Arrays.asList(compra1, compra2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/compras")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].valorTotal").value(100.0))
        .andExpect(jsonPath("$[1].valorTotal").value(200.0));

    verify(compraService, times(1)).getAllComprasSortedByValue();
  }

  @Test
  void testGetMaiorCompraPorAno() throws Exception {
    when(compraService.getMaiorCompraPorAno(2024)).thenReturn(compra2);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/maior-compra/2024")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.valorTotal").value(200.0));

    verify(compraService, times(1)).getMaiorCompraPorAno(2024);
  }

  @Test
  void testGetMaiorCompraPorAnoNotFound() throws Exception {
    when(compraService.getMaiorCompraPorAno(2024)).thenThrow(new RuntimeException("Nenhuma compra encontrada para o ano: 2024"));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/maior-compra/2024")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.detail").value("Nenhuma compra encontrada para o ano: 2024"));

    verify(compraService, times(1)).getMaiorCompraPorAno(2024);
  }

  @Test
  void testGetClientesFieis() throws Exception {
    when(compraService.getClientesFieis()).thenReturn(Arrays.asList(cliente));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes-fieis")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].nome").value("Cliente Teste"));

    verify(compraService, times(1)).getClientesFieis();
  }

  @Test
  void testGetRecomendacaoVinho() throws Exception {
    when(clienteRepository.findById("123")).thenReturn(Optional.of(cliente));
    when(compraService.getRecomendacaoDeVinho(cliente)).thenReturn(produto);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/recomendacao/123/tipo")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.tipo_vinho").value("Tinto"));

    verify(clienteRepository, times(1)).findById("123");
    verify(compraService, times(1)).getRecomendacaoDeVinho(cliente);
  }

  @Test
  void testGetRecomendacaoVinhoNotFound() throws Exception {
    when(clienteRepository.findById("123")).thenReturn(Optional.empty());

    mockMvc.perform(MockMvcRequestBuilders.get("/api/recomendacao/123/tipo")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    verify(clienteRepository, times(1)).findById("123");
  }
}
