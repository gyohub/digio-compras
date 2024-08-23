package com.gx2.digio.gyowanny.compras.repository;

import com.gx2.digio.gyowanny.compras.entity.Cliente;
import com.gx2.digio.gyowanny.compras.entity.Compra;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
  List<Compra> findAllByOrderByValorTotalAsc();

  @Query("SELECT c FROM Compra c " +
      "JOIN FETCH c.cliente cli " +
      "JOIN FETCH c.produto p " +
      "WHERE p.anoCompra = :ano " +
      "ORDER BY c.valorTotal DESC " +
      "LIMIT 1")
  Optional<Compra> findFirstByAnoCompraOrderByValorTotalDesc(@Param("ano") Integer ano);

  @Query("SELECT c.produto.tipo FROM Compra c WHERE c.cliente = :cliente GROUP BY c.produto.tipo ORDER BY COUNT(c.produto.tipo) DESC LIMIT 1")
  Optional<String> findTipoVinhoMaisCompradoByCliente(@Param("cliente") Cliente cliente);

  @Query("SELECT c.cliente AS cliente, COUNT(c) AS totalCompras, SUM(c.valorTotal) AS totalGasto " +
      "FROM Compra c " +
      "GROUP BY c.cliente " +
      "ORDER BY totalCompras DESC, totalGasto DESC")
  List<ClienteFielProjection> findTop3ClientesFieis(Pageable pageable);

}