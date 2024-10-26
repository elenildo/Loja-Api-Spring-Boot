package com.dev.loja.service;

import com.dev.loja.config.TokenService;
import com.dev.loja.dto.AuthenticationDTO;
import com.dev.loja.dto.LoginResponseDTO;
import com.dev.loja.dto.RegisterDTO;
import com.dev.loja.dto.UserDtoSaida;
import com.dev.loja.enums.UserRole;
import com.dev.loja.exception.DuplicatedEntityException;
import com.dev.loja.model.User;
import com.dev.loja.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthenticationService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private TokenService tokenService;
    public LoginResponseDTO login(AuthenticationDTO data) {
        var userNamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(userNamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        var user = userRepository.findUserByLogin(data.login()).get();
        var userDtoSaida = new UserDtoSaida(user);
        return new LoginResponseDTO(userDtoSaida, token);
    }

    public String register(RegisterDTO data) {
        if(userRepository.findByLogin(data.login()) != null){
            throw new DuplicatedEntityException("Já existe um usuário com esse login");
        }
        String encriptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User user = new User(data.nome(), data.sobrenome(), data.login(), encriptedPassword, UserRole.USER);
        userRepository.save(user);

        return "Usuario Criado";
    }
}
