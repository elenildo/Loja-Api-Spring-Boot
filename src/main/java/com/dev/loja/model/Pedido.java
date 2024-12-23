package com.dev.loja.model;

import com.dev.loja.dto.PedidoDtoEntrada;
import com.dev.loja.enums.FormaPagamento;
import com.dev.loja.enums.PedidoStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numero;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itens = new ArrayList<>();
    private FormaPagamento formaPagamento;
    private BigDecimal frete;
    private BigDecimal descontos;
    private BigDecimal subtotal;
    private BigDecimal total;
    private PedidoStatus pedidoStatus;
    private LocalDateTime data;
    @Transient
    private String cep;

    public Pedido(PedidoDtoEntrada pedidoDtoEntrada){
        this.pedidoStatus = pedidoDtoEntrada.pedidoStatus();
    }

}
