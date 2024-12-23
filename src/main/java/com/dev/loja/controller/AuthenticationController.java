package com.dev.loja.controller;

import com.dev.loja.config.TokenService;
import com.dev.loja.dto.AuthenticationDTO;
import com.dev.loja.dto.RegisterDTO;
import com.dev.loja.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthenticationController {
    private AuthenticationService authenticationService;
    private TokenService tokenService;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data){
        return new ResponseEntity<>(authenticationService.login(data), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data){
        return new ResponseEntity<>(authenticationService.register(data), HttpStatus.OK);
    }

    @GetMapping("/")
    public String tokenCheck(@RequestParam String token){
        return tokenService.validateToken(token);
    }

}
