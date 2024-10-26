package com.dev.loja.service;

import com.dev.loja.dto.EnderecoDtoSaida;
import com.dev.loja.dto.UserDto;
import com.dev.loja.dto.UserDtoSaida;
import com.dev.loja.exception.BadRequestException;
import com.dev.loja.exception.EntityNotFoundException;
import com.dev.loja.model.Endereco;
import com.dev.loja.model.User;
import com.dev.loja.repository.EnderecoRepository;
import com.dev.loja.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private EnderecoRepository enderecoRepository;
    private BeanUtilsBean beanUtilsBean;

    public Page<UserDto> listarTudo(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return new PageImpl<>(users.stream().map(UserDto::new).toList(), pageable, users.getTotalElements());
    }
    public EnderecoDtoSaida adicionarEndereco(UserDetails userDetails, Endereco endereco) {
        var user = buscarUsuarioPorLogin(userDetails.getUsername());

        if(endereco.getPrincipal()) enderecoRepository.desativarEnderecos(user.getId());
        endereco.setUser(user);
        enderecoRepository.save(endereco);
        return new EnderecoDtoSaida(endereco);
    }

    public EnderecoDtoSaida editarEndereco(UserDetails userDetails, Endereco endereco, Long id) throws InvocationTargetException, IllegalAccessException {
        var user = buscarUsuarioPorLogin(userDetails.getUsername());
        var end = buscarEnderecoPorId(id);

        if(!Objects.equals(end.getUser(), user))
            throw new BadRequestException("O endereço não pertence ao usuário logado");

        if(endereco.getPrincipal()) enderecoRepository.desativarEnderecos(user.getId());

        beanUtilsBean.copyProperties(end, endereco);
        return new EnderecoDtoSaida(enderecoRepository.save(end));
    }

    public UserDtoSaida buscarPorId(Long id) {
        var busca = userRepository.findById(id);
        if(busca.isEmpty())
           throw new EntityNotFoundException("Usuário não encontrado");

        return new UserDtoSaida(busca.get());
    }

    public UserDtoSaida alterarUser(UserDto user) {
        var usuario = buscarUsuarioPorLogin(user.usuarioEmail);

        usuario.setRole(user.papel);
        return new UserDtoSaida(userRepository.save(usuario));
    }

    public Page<UserDto> buscarUsuarioPorEmail(String email, Pageable pageable) {
        var busca = userRepository.findByLoginContaining(email, pageable);
        return new PageImpl<>(busca.stream().map(UserDto::new).toList(), pageable, busca.getTotalElements());
    }

    private User buscarUsuarioPorLogin(String login) {
        var busca = userRepository.findUserByLogin(login);
        if (busca.isEmpty())
            throw new EntityNotFoundException("Usuário não encontrado");
        return busca.get();
    }

    private Endereco buscarEnderecoPorId(Long id) {
        var busca = enderecoRepository.findById(id);
        if (busca.isEmpty())
            throw new EntityNotFoundException("Endereço não encontrado");
        return busca.get();
    }

    public UserDtoSaida mostrarPerfil() {
        var usuario = buscarUsuarioPorLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        return new UserDtoSaida(usuario);
    }
}
