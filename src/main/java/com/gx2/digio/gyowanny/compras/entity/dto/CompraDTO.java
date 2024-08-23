package com.gx2.digio.gyowanny.compras.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraDTO {
  @JsonProperty("codigo")
  private Long produtoId;
  private Integer quantidade;
  private Double valorTotal;
  private LocalDate anoCompra;

  // Getters and Setters
}