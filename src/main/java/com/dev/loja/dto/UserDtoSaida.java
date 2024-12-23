package com.dev.loja.dto;

import com.dev.loja.controller.UserController;
import com.dev.loja.enums.UserRole;
import com.dev.loja.model.Endereco;
import com.dev.loja.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@NoArgsConstructor
public class UserDtoSaida extends RepresentationModel<UserDtoSaida> {

    public String nome;
    public String sobrenome;
    public String email;
    public EnderecoDtoSaida endereco;
    public UserRole role;

    public UserDtoSaida(User user){
        if(user != null){
            this.nome = user.getNome();
            this.sobrenome = user.getSobrenome();
            this.email = user.getLogin();
            var endereco = user.getEnderecos().stream().filter(Endereco::getPrincipal).findFirst();
            this.endereco = endereco.isEmpty() ? null : new EnderecoDtoSaida(endereco.get());
            this.role = user.getRole();
            this.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                    .buscarPorId(user.getId())).withSelfRel());
            this.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                    .listarTudo(null)).withRel(IanaLinkRelations.COLLECTION));
        }
    }
}
