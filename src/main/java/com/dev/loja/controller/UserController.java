package com.dev.loja.controller;

import com.dev.loja.dto.UserDto;
import com.dev.loja.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("admin/usuarios")
public class UserController {
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> listarTudo(Pageable pageable){
        return new ResponseEntity<>(userService.listarTudo(pageable), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        return new ResponseEntity<>(userService.buscarPorId(id), HttpStatus.OK);
    }

    @PostMapping("roles")
    public ResponseEntity<?> alterarUser(@RequestBody @Valid UserDto userDto){
        return new ResponseEntity<>(userService.alterarUser(userDto), HttpStatus.OK);
    }

    @PostMapping("busca")
    public ResponseEntity<?> buscarPorLogin(@RequestParam String email, Pageable pageable){
        return new ResponseEntity<>(userService.buscarUsuarioPorEmail(email, pageable), HttpStatus.OK);
    }
}
