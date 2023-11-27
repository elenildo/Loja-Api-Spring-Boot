package com.dev.loja.dto;

import com.dev.loja.enums.PedidoStatus;
import com.dev.loja.model.Pedido;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PedidoDtoSaida {
    public String numero;
    public String data;
    public String formaPagamento;
    public String frete;
    public String descontos;
    public String subtotal;
    public String total;
    public PedidoStatus status;
    public UserDtoSaida cliente;
    public String cep;
    public List<ItemPedidoDtoSaida> itens;

    public PedidoDtoSaida(Pedido pedido){
        this.numero = (pedido.getNumero() != null)? String.format("%05d", Integer.parseInt(pedido.getNumero().toString())): null;
        this.data = pedido.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        this.formaPagamento = pedido.getFormaPagamento().toString();
        this.frete = pedido.getFrete().toString();
        this.descontos = (pedido.getDescontos() != null)? pedido.getDescontos().toString(): "0.0";
        this.subtotal = pedido.getSubtotal().toString();
        this.total = pedido.getTotal().toString();
        this.status = pedido.getPedidoStatus();
        this.cliente = new UserDtoSaida(pedido.getUser());
        this.cep = pedido.getCep();
        this.itens = pedido.getItens().stream().map(ItemPedidoDtoSaida::new).toList();
    }

}
