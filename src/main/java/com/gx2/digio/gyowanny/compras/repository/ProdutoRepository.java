package com.gx2.digio.gyowanny.compras.repository;

import com.gx2.digio.gyowanny.compras.entity.Produto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
  Optional<Produto> findFirstByTipo(String tipo);
}
