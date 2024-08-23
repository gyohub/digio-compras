package com.gx2.digio.gyowanny.compras.entity.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteCompraDTO {
  private Long clienteId;
  private String nome;
  private String cpf;
  private List<CompraDTO> compras;

  // Getters and Setters
}
