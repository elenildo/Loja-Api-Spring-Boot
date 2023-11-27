package com.dev.loja.service;

import com.dev.loja.dto.PedidoDtoEntrada;
import com.dev.loja.dto.PedidoDtoSaida;
import com.dev.loja.dto.ProdutoDtoSaida;
import com.dev.loja.exception.EntityNotFoundException;
import com.dev.loja.model.Pedido;
import com.dev.loja.model.Produto;
import com.dev.loja.repository.PedidoRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

@Service
@AllArgsConstructor
public class PedidoService {
    private PedidoRepository pedidoRepository;
    private BeanUtilsBean beanUtilsBean;

    public Page<PedidoDtoSaida> listarPedidos(UserDetails userDetails, Pageable pageable) {
        Page<Pedido> pedidos = pedidoRepository.findAll(pageable);
        return new PageImpl<>(pedidos.stream().map(PedidoDtoSaida::new)
                .toList(), pageable, pedidos.getTotalElements());
    }

    public ResponseEntity<?> alterarStatus(UserDetails userDetails, Long id, PedidoDtoEntrada pedidoDtoEntrada) throws InvocationTargetException, IllegalAccessException {
        var pedido = buscarPedidoPorId(id);

        beanUtilsBean.copyProperties(pedido, new Pedido(pedidoDtoEntrada));
        return new ResponseEntity<>(new PedidoDtoSaida(pedidoRepository.save(pedido)), HttpStatus.OK);
    }

    private Pedido buscarPedidoPorId(Long id){
        var busca = pedidoRepository.findById(id);
        if (busca.isEmpty())
            throw new EntityNotFoundException("Pedido não encontrado");
        return busca.get();
    }
}
