package com.gx2.digio.gyowanny.compras.repository;

import com.gx2.digio.gyowanny.compras.entity.Cliente;

public interface ClienteFielProjection {
  Cliente getCliente();
  Long getTotalCompras();
  Double getTotalGasto();
}
