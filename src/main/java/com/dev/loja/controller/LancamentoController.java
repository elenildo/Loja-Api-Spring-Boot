package com.dev.loja.controller;

import com.dev.loja.model.Lancamento;
import com.dev.loja.service.LancamentoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("admin/estoque")
public class LancamentoController {
    private LancamentoService lancamentoService;

    @PostMapping
    public ResponseEntity<?> lancarProduto(@RequestBody @Valid Lancamento lancamento,
                                           @AuthenticationPrincipal UserDetails userDetails){
        return lancamentoService.lancarProduto(lancamento, userDetails);
    }

    @GetMapping("{produtoId}")
    public ResponseEntity<?> consultarEstoque(@PathVariable Long produtoId){
        return lancamentoService.consultarEstoque(produtoId);
    }
}
