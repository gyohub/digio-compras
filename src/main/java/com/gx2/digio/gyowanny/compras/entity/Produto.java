package com.gx2.digio.gyowanny.compras.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Produto {
  @Id
  @JsonProperty("codigo")
  private Long id;
  @JsonProperty("tipo_vinho")
  private String tipo;
  private Double preco;
  private String safra;
  @JsonProperty("ano_compra")
  private Integer anoCompra;
}
