package com.gx2.digio.gyowanny.compras.loader;

import com.gx2.digio.gyowanny.compras.entity.Cliente;
import com.gx2.digio.gyowanny.compras.entity.Compra;
import com.gx2.digio.gyowanny.compras.entity.Produto;
import com.gx2.digio.gyowanny.compras.entity.dto.ClienteCompraDTO;
import com.gx2.digio.gyowanny.compras.entity.dto.CompraDTO;
import com.gx2.digio.gyowanny.compras.repository.ClienteRepository;
import com.gx2.digio.gyowanny.compras.repository.CompraRepository;
import com.gx2.digio.gyowanny.compras.repository.ProdutoRepository;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DataLoader {

  private static final String PRODUTOS_URL = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/produtos-mnboX5IPl6VgG390FECTKqHsD9SkLS.json";
  private static final String CLIENTES_COMPRAS_URL = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/clientes-Vz1U6aR3GTsjb3W8BRJhcNKmA81pVh.json";

  @Autowired
  private ProdutoRepository produtoRepository;

  @Autowired
  private ClienteRepository clienteRepository;

  @Autowired
  private CompraRepository compraRepository;

  @Autowired
  private RestTemplate restTemplate;

  public void run() {
    loadProdutos();
    loadClientesECompras();
  }

  private void loadProdutos() {
    Produto[] produtos = restTemplate.getForObject(PRODUTOS_URL, Produto[].class);
    if (produtos != null) {
      produtoRepository.saveAll(Arrays.asList(produtos));
    } else {
      throw new RuntimeException("Falha ao carregar a lista de produtos");
    }
  }

  private void loadClientesECompras() {
    var clientesCompras = List.of(restTemplate.getForObject(CLIENTES_COMPRAS_URL, ClienteCompraDTO[].class));

    if (clientesCompras != null) {
      for (ClienteCompraDTO dto : clientesCompras) {
        Cliente cliente = Cliente.builder()
            .cpf(dto.getCpf())
            .nome(dto.getNome())
            .build();
        clienteRepository.save(cliente);

        for (CompraDTO compraDTO : dto.getCompras()) {
          Produto produto = produtoRepository.findById(compraDTO.getProdutoId())
              .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + compraDTO.getProdutoId()));

          Compra compra = Compra.builder()
              .cliente(cliente)
              .produto(produto)
              .quantidade(compraDTO.getQuantidade())
              .valorTotal(compraDTO.getQuantidade()*produto.getPreco())
              .build();
          compraRepository.save(compra);
        }
      }
    } else {
      throw new RuntimeException("Falha ao carregar a lista de clientes e compras");
    }
  }
}