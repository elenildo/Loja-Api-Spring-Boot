package com.dev.loja.repository;

import com.dev.loja.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{

    @Query("SELECT p FROM Produto p WHERE p.categoria.nome = :categoria")
    Page<Produto> getProdutoByCategoriaNome(String categoria, Pageable pageable);

    Page<Produto> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
