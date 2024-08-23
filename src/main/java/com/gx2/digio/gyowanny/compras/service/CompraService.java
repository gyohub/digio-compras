package com.gx2.digio.gyowanny.compras.service;

import com.gx2.digio.gyowanny.compras.entity.Cliente;
import com.gx2.digio.gyowanny.compras.entity.Compra;
import com.gx2.digio.gyowanny.compras.entity.Produto;
import java.util.List;

public interface CompraService {
  List<Compra> getAllComprasSortedByValue();
  Compra getMaiorCompraPorAno(int ano);
  List<Cliente> getClientesFieis();
  Produto getRecomendacaoDeVinho(Cliente cliente);
}
