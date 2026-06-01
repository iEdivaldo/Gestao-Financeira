package com.gestaofinanceira.backend.services;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gestaofinanceira.backend.dto.Usuario.LoginRequestDTO;
import com.gestaofinanceira.backend.dto.Usuario.LoginResponseDTO;
import com.gestaofinanceira.backend.dto.Usuario.UsuarioRequestDTO;
import com.gestaofinanceira.backend.dto.Usuario.UsuarioResponseDTO;
import com.gestaofinanceira.backend.exception.BusinessException;
import com.gestaofinanceira.backend.model.LogAuditoria;
import com.gestaofinanceira.backend.model.Usuario;
import com.gestaofinanceira.backend.model.enums.Perfil;
import com.gestaofinanceira.backend.repository.LogAuditoriaRepository;
import com.gestaofinanceira.backend.repository.UsuarioRepository;
import com.gestaofinanceira.backend.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final LogAuditoriaRepository logAuditoriaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // login
    @Transactional // garante que a operação de login seja atômica, ou seja, ou tudo ocorre com sucesso ou nada é persistido
    public LoginResponseDTO login(LoginRequestDTO dto, HttpServletRequest request) {
        // o authenticationManager validao e-mail + senha automaticamente
        // se credenciais forem invalidas lançará o BadCredentialsException, que é tratado no GlobalExceptionHandler
        // se forem validas, o fluxo continua normalmente
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.senha()));

        // se chegou aqui, é porque as credenciais são válidas
        Usuario usuario = usuarioRepository.findByEmail(dto.email()).orElseThrow(); // não precisa tratar o Optional aqui, pois se o email não existir, a autenticação já falhou e lançou uma exceção

        // Gerar token JWT
        String token = jwtUtil.gerarToken(usuario);

        // registra o log de auditoria de login
        salvarLog("LOGIN", null, null, "{\"email\": \"" + dto.email() + "\"}", request, usuario);
    
        log.info("Login realizado: {}", usuario.getEmail());

        return new LoginResponseDTO(
            token,
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getPerfil()
        );
    }

    // registro
    @Transactional
    public UsuarioResponseDTO registrar(UsuarioRequestDTO dto, HttpServletRequest request) {
        // Verificar se o email já está registrado
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new BusinessException("Email já registrado");
        }

        // Criar novo usuário
        Usuario usuario = Usuario.builder()
            .nome(dto.nome())
            .email(dto.email())
            .senha(passwordEncoder.encode(dto.senha())) // criptografa a senha antes de salvar
            .perfil(dto.perfil() != null ? dto.perfil() : Perfil.USUARIO) // define perfil padrão como USUARIO se não for fornecido
            .ativo(true) // define o usuário como ativo por padrão
            .build();

        // Salvar usuário no banco de dados
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // registra o log de auditoria de registro
        salvarLog("REGISTRO", "usuarios", usuarioSalvo.getId().toString(), "{\"email\": \"" + usuarioSalvo.getEmail() + "\"}", request, usuarioSalvo);
    
        log.info("Novo usuario registrado: {}", usuarioSalvo.getEmail());

        return UsuarioResponseDTO.de(usuarioSalvo);
    }

    @Transactional
    public void logout(LogAuditoria logEntry) {
        // Salvar o log de auditoria de logout
        logAuditoriaRepository.save(logEntry);
        log.info("Logout realizado: {}", logEntry.getUsuario().getEmail());
    }
    
    // metodo auxiliar o salvar log de auditoria
    private void salvarLog(String acao, String entidade, String entidadeId, String detalhe, HttpServletRequest request, Usuario usuario) {
        try {
            LogAuditoria log = LogAuditoria.builder()
                .acao(acao)
                .entidade(entidade)
                // converte a string do id para UUID, se for nula, passa null
                .entidadeId(entidadeId != null ? UUID.fromString(entidadeId) : null)
                .detalhe(detalhe)
                // pega id do cliente da requisicao HTTP
                .ip(request.getRemoteAddr())
                .usuario(usuario)
                .build();

                logAuditoriaRepository.save(log);
        } catch (Exception e) {
            log.warn("Falha ao salvar log de auditoria: {}", e.getMessage());
        }
    }
}
