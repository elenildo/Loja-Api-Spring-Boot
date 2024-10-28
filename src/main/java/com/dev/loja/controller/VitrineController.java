package com.dev.loja.controller;

import com.dev.loja.service.ProdutoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping({"", "/"})
public class VitrineController {
    private ProdutoService produtoService;

    @GetMapping("home/busca/{nome}")
    public ResponseEntity<?> buscarProdutosPorNome(@PathVariable String nome, Pageable pageable){
        return new ResponseEntity<>(produtoService.buscarPorNome(nome, pageable), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> index(Pageable pageable){
        return home(pageable);
    }

    @GetMapping("home")
    public ResponseEntity<?> home(Pageable pageable){
        return new ResponseEntity<>(produtoService.listarTudoVitrine(pageable), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        return produtoService.buscarPorIdHome(id);
    }


    @GetMapping("home/{categoria}")
    public ResponseEntity<?> produtosPorCategoria(@PathVariable String categoria, Pageable pageable){
        return new ResponseEntity<>(produtoService.produtosPorCategoriaNome(categoria, pageable), HttpStatus.OK);
    }

}
