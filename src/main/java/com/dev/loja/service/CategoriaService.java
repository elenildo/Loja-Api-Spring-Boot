package com.dev.loja.service;

import com.dev.loja.dto.CategoriaDto;
import com.dev.loja.dto.CategoriaDtoSaida;
import com.dev.loja.exception.DuplicatedEntityException;
import com.dev.loja.exception.EntityNotFoundException;
import com.dev.loja.model.Categoria;
import com.dev.loja.repository.CategoriaRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoriaService {
    private CategoriaRepository categoriaRepository;

    public Page<CategoriaDtoSaida> listarTudo(Pageable pageable) {
        Page<Categoria> cats = categoriaRepository.findAll(pageable);
        return new PageImpl<>(cats.stream().map(
                CategoriaDtoSaida::new
        ).toList(), pageable, cats.getTotalElements());
    }

    public CategoriaDtoSaida novo(CategoriaDto categoriaDto) {
            var busca = categoriaRepository.findByNome(categoriaDto.nome().trim());
            if(busca.isPresent())
                throw new DuplicatedEntityException("Erro: Já existe a categoria '"+categoriaDto.nome()+"'");
            var categ = categoriaRepository.save(new Categoria(categoriaDto));

        return new CategoriaDtoSaida(categ);
    }

    public CategoriaDtoSaida buscarPorId(Long id) {
        var categoria = buscarCategoriaPorId(id);
        return new CategoriaDtoSaida(categoria);
    }

    @CacheEvict(cacheNames = {"produtos", "produtos-vitrine"}, allEntries = true)
    public CategoriaDtoSaida editar(Long id, CategoriaDto categoriaDto) {
        var categoria = buscarCategoriaPorId(id);
        categoria.setNome(categoriaDto.nome());
        categoriaRepository.save(categoria);
        return new CategoriaDtoSaida(categoria);
    }

    private Categoria buscarCategoriaPorId(Long id){
        var busca = categoriaRepository.findById(id);
        if(busca.isEmpty())
            throw new EntityNotFoundException("Recurso não encontrado");

        return busca.get();
    }

    public Page<CategoriaDtoSaida> buscarCategoriaPorNome(String nome, Pageable pageable) {
        Page<Categoria> cats = categoriaRepository.findByNomeContaining(nome, pageable);
        return new PageImpl<>(cats.stream().map(
                CategoriaDtoSaida::new
        ).toList(), pageable, cats.getTotalElements());
    }
}
