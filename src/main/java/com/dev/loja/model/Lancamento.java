package com.dev.loja.model;

import com.dev.loja.enums.ProdutoStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Produto produto;

    private Long codigoBarras;

    @Transient
    private Integer quantidade;

    private LocalDateTime dataLancamento;

    private LocalDateTime dataSaida;

    private ProdutoStatus produtoStatus = ProdutoStatus.DISPONIVEL;

    private String responsavel;

}
