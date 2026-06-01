package com.gestaofinanceira.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestaofinanceira.backend.dto.Usuario.LoginRequestDTO;
import com.gestaofinanceira.backend.dto.Usuario.LoginResponseDTO;
import com.gestaofinanceira.backend.dto.Usuario.UsuarioRequestDTO;
import com.gestaofinanceira.backend.dto.Usuario.UsuarioResponseDTO;
import com.gestaofinanceira.backend.model.LogAuditoria;
import com.gestaofinanceira.backend.model.Usuario;
import com.gestaofinanceira.backend.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto, HttpServletRequest request) {
        return ResponseEntity.ok(authService.login(dto, request));
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registro(@RequestBody @Valid UsuarioRequestDTO dto, HttpServletRequest request) {
        System.out.println("Recebendo requisição de registro: " + dto);
        UsuarioResponseDTO response = authService.registrar(dto, request);
        System.out.println("Usuário registrado: " + response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario usuario) {
            LogAuditoria logEntry = LogAuditoria.builder()
                    .acao("LOGOUT")
                    .ip(request.getRemoteAddr())
                    .usuario(usuario)
                    .build();

            authService.logout(logEntry);
        }
        return ResponseEntity.noContent().build();
    }
}
