package com.dev.loja.dto;

import com.dev.loja.controller.VitrineController;
import com.dev.loja.model.Produto;
import jakarta.persistence.Lob;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.io.Serializable;
import java.util.List;

public class ProdutoDtoVitrine extends RepresentationModel<ProdutoDtoVitrine> implements Serializable {

    public String id;
    public String categoria;
    public String nome;
    public String precoVenda;
    @Lob
    public String descricao;
    public boolean disponivel;

    public List<ImagemDtoSaida> imagens;

    public ProdutoDtoVitrine(Produto produto){
        this.id = String.valueOf(produto.getId());
        this.categoria = produto.getCategoria().getNome();
        this.nome = produto.getNome();
        this.precoVenda = produto.getPrecoVenda().toString();
        this.descricao = produto.getDescricao();
        this.disponivel = produto.getEstoqueAtual() > 0;
        this.imagens = produto.getImagens().stream().map(ImagemDtoSaida::new).toList();
        this.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VitrineController.class)
                        .buscarPorId(produto.getId())).withSelfRel());
        this.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VitrineController.class)
                        .home(null)).withRel(IanaLinkRelations.COLLECTION));

    }

}
