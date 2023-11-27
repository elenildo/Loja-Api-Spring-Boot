package com.dev.loja.controller;

import com.dev.loja.dto.PedidoDtoEntrada;
import com.dev.loja.service.PedidoService;
import com.dev.loja.service.VendaService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

@RestController
@AllArgsConstructor
@RequestMapping("admin/pedidos")
public class PedidoController {
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<?> listarPedidos(@AuthenticationPrincipal UserDetails userDetails, Pageable pageable){
        return new ResponseEntity<>(pedidoService.listarPedidos(userDetails, pageable), HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> alterarStatus(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id,
                                           @RequestBody PedidoDtoEntrada pedidoDtoEntrada) throws InvocationTargetException, IllegalAccessException {
        return new ResponseEntity<>(pedidoService.alterarStatus(userDetails, id, pedidoDtoEntrada), HttpStatus.OK);

    }
}
