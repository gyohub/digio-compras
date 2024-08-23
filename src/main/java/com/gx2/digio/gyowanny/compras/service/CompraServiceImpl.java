package com.gx2.digio.gyowanny.compras.service;

import com.gx2.digio.gyowanny.compras.entity.Cliente;
import com.gx2.digio.gyowanny.compras.entity.Compra;
import com.gx2.digio.gyowanny.compras.entity.Produto;
import com.gx2.digio.gyowanny.compras.repository.ClienteRepository;
import com.gx2.digio.gyowanny.compras.repository.CompraRepository;
import com.gx2.digio.gyowanny.compras.repository.ProdutoRepository;
import com.gx2.digio.gyowanny.compras.repository.ClienteFielProjection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CompraServiceImpl implements CompraService {

  @Autowired
  private CompraRepository compraRepository;

  @Autowired
  private ClienteRepository clienteRepository;

  @Autowired
  private ProdutoRepository produtoRepository;

  /**
   * Retorna uma lista de compras ordenadas de forma crescente por valor total.
   */
  public List<Compra> getAllComprasSortedByValue() {
    return compraRepository.findAllByOrderByValorTotalAsc();
  }

  /**
   * Retorna a maior compra do ano especificado.
   *
   * @param ano Ano para a busca da maior compra.
   * @return Compra com maior valor total no ano informado.
   */
  public Compra getMaiorCompraPorAno(int ano) {
    return compraRepository.findFirstByAnoCompraOrderByValorTotalDesc(ano)
        .orElseThrow(() -> new RuntimeException("Nenhuma compra encontrada para o ano: " + ano));
  }

  /**
   * Retorna os 3 clientes mais fiéis, com base no número de compras e valores totais.
   *
   * @return Lista com os 3 clientes mais fiéis.
   */
  public List<Cliente> getClientesFieis() {
    return compraRepository.findTop3ClientesFieis(PageRequest.of(0, 3))
        .stream()
        .map(ClienteFielProjection::getCliente)
        .collect(Collectors.toList());
  }

  /**
   * Retorna uma recomendação de vinho baseada nos tipos de vinho que o cliente mais compra.
   *
   * @param cliente Cliente para o qual a recomendação será feita.
   * @return Produto recomendado baseado nas compras anteriores do cliente.
   */
  public Produto getRecomendacaoDeVinho(Cliente cliente) {
    String tipoVinhoMaisComprado = compraRepository.findTipoVinhoMaisCompradoByCliente(cliente)
        .orElseThrow(() -> new RuntimeException("Nenhum tipo de vinho encontrado para o cliente: " +
            cliente.getNome()));

    return produtoRepository.findFirstByTipo(tipoVinhoMaisComprado)
        .orElseThrow(() -> new RuntimeException("Nenhum produto encontrado para o tipo de vinho: " +
            tipoVinhoMaisComprado));
  }
}
