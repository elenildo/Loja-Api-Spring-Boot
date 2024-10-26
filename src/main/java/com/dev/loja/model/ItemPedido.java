package com.dev.loja.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    private Pedido pedido;
    @ManyToOne
    private Produto produto;
    private Long codigoBarras;
    private int quantidade;
    private BigDecimal subtotal;

    public BigDecimal getSubtotal(){
        return produto.getPrecoVenda().multiply(new BigDecimal(quantidade));
    }
}
