package com.dev.loja.service;

import com.dev.loja.dto.CategoriaDto;
import com.dev.loja.exception.EntityNotFoundException;
import com.dev.loja.model.Categoria;
import com.dev.loja.repository.CategoriaRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoriaService {
    private CategoriaRepository categoriaRepository;

    public ResponseEntity<?> listarTudo() {
        return new ResponseEntity<>(categoriaRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<?> novo(CategoriaDto categoriaDto) {
            var busca = categoriaRepository.findByNome(categoriaDto.nome().trim());
            if(busca.isPresent())
                return new ResponseEntity<>("Erro: Já existe a categoria '"+categoriaDto.nome()+"'", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(categoriaRepository.save(new Categoria(categoriaDto)), HttpStatus.CREATED);
    }

    public ResponseEntity<?> buscarPorId(Long id) {
        var busca = categoriaRepository.findById(id);
        if(busca.isEmpty())
            throw new EntityNotFoundException("Recurso não encontrado");

        return new ResponseEntity<>(busca.get(), HttpStatus.OK);
    }
}
