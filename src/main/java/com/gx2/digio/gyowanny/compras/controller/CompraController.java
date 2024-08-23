package com.gx2.digio.gyowanny.compras.controller;

import com.gx2.digio.gyowanny.compras.entity.Cliente;
import com.gx2.digio.gyowanny.compras.entity.Compra;
import com.gx2.digio.gyowanny.compras.entity.Produto;
import com.gx2.digio.gyowanny.compras.repository.ClienteRepository;
import com.gx2.digio.gyowanny.compras.service.CompraService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CompraController {
  @Autowired
  private CompraService compraService;
  @Autowired
  private ClienteRepository clienteRepository;

  @GetMapping("/compras")
  public List<Compra> getCompras() {
    return compraService.getAllComprasSortedByValue();
  }

  @GetMapping("/maior-compra/{ano}")
  public ResponseEntity<Compra> getMaiorCompraPorAno(@PathVariable Integer ano) {
    try {
      return ResponseEntity.ok(compraService.getMaiorCompraPorAno(ano));
    }catch(Exception e) {
      var problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
      problemDetail.setDetail(e.getMessage());
      return ResponseEntity.of(problemDetail).build();
    }
  }

  @GetMapping("/clientes-fieis")
  public List<Cliente> getClientesFieis() {
    return compraService.getClientesFieis();
  }

  @GetMapping("/recomendacao/{clienteId}/tipo")
  public ResponseEntity<Produto> getRecomendacaoVinho(@PathVariable(name="clienteId") String cpf) {
    return clienteRepository.findById(cpf).map(cliente -> {
      Produto recomendacao = compraService.getRecomendacaoDeVinho(cliente);
      return ResponseEntity.ok(recomendacao);
    }).orElse(ResponseEntity.notFound().build());
  }
}
