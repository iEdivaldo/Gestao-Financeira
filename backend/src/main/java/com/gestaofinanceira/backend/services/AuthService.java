package com.gestaofinanceira.backend.services;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gestaofinanceira.backend.model.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class AuthService {

    @Value("${api.security.token.secret}")
    private String jwtSecret;

    private SecretKey secretKey;

    @Autowired
    private UsuarioService usuarioService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean autenticar(String email, String senha) {
        Usuario usuario = usuarioService.obterUsuarioPorEmail(email);
        if (usuario != null && usuario.getAtivo()) {
            return passwordEncoder.matches(senha, usuario.getSenha());
        }
        return false;
    }
    
    public String gerarToken(Usuario usuario) {
        Instant agora = Instant.now();

        return Jwts.builder()
            .subject(usuario.getEmail())
            .claim("id", usuario.getId().toString())
            .claim("nome", usuario.getNome())
            .claim("perfil", usuario.getPerfil().name())
            .issuedAt(Date.from(agora))
            .expiration(Date.from(agora.plus(1, ChronoUnit.HOURS)))
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact();
    }

    public String ValidateToken(String token) {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(
            jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
    }
}
