package com.dev.loja.controller;

import com.dev.loja.dto.CategoriaDto;
import com.dev.loja.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/categorias")
@AllArgsConstructor
public class CategoriaController {
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<?> listarTudo(Pageable pageable){
        return new ResponseEntity<>(categoriaService.listarTudo(pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> novo(@RequestBody @Valid CategoriaDto categoriaDto){
        return new ResponseEntity<>(categoriaService.novo(categoriaDto), HttpStatus.CREATED);
    }
    @PutMapping("{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody @Valid CategoriaDto categoriaDto){
        return new ResponseEntity<>(categoriaService.editar(id, categoriaDto), HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        return new ResponseEntity<>(categoriaService.buscarPorId(id), HttpStatus.OK);
    }
    @PostMapping("busca")
    public ResponseEntity<?> buscarPorNome(@RequestParam String nome, Pageable pageable){
        return new ResponseEntity<>(categoriaService.buscarCategoriaPorNome(nome, pageable), HttpStatus.OK);
    }
}
