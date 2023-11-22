package com.dev.loja.repository;

import com.dev.loja.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>, PagingAndSortingRepository<Categoria, Long> {
    Optional<Categoria> findByNome(String nome);
    Page<Categoria> findByNomeContaining(String nome, Pageable pageable);


}
