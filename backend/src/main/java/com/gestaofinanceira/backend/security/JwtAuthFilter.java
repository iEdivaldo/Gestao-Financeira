package com.gestaofinanceira.backend.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor // @RequiredArgsConstructor do Lombok para gerar um construtor com os campos finais (final) - isso é útil para injeção de dependências
@Slf4j // @Slf4j do Lombok para gerar um logger - isso é útil para registrar informações de depuração e erros no filtro de autenticação
public class JwtAuthFilter extends OncePerRequestFilter { // OncePerRequestFilter é uma classe do Spring que garante que o filtro seja executado apenas uma vez por requisição, mesmo que haja múltiplos filtros na cadeia de filtros

    private final JwtUtil jwtUtil; // Injeção de dependência do JwtUtil para usar seus métodos de geração e validação de tokens
    private final UserDetailsService userDetailsService; // Injeção de dependência do UserDetailsService para carregar os detalhes do usuário a partir do token

    @Override // Sobrescreve o método do filtro para interceptar as requisições e validar o token JWT
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // - Passo 1: obter header Authorization - //
        final String authHeader = request.getHeader("Authorization"); // Obtém o valor do header "Authorization" da requisição, onde o token JWT deve ser enviado pelo cliente

        // - Passo 2: validar formato do header - //
        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // Verifica se o header está presente e começa com "Bearer " (formato padrão para tokens JWT)
            filterChain.doFilter(request, response); // Se o header não estiver presente ou tiver formato inválido, continua a cadeia de filtros sem autenticar o usuário
            return; // Retorna para evitar que o código abaixo seja executado
        }

        // - Passo 3: extrair token do header - //
        final String token = authHeader.substring(7); // Extrai o token removendo a parte "Bearer " (7 caracteres)
        final String email;

        try {
            // - Passo 4: extrair o email do token - //
            email = jwtUtil.extrairEmail(token); // Usa o método do JwtUtil para extrair o email do usuário a partir do token
        } catch (Exception e) {
            // Se ocorrer qualquer erro ao extrair o email (token inválido, expirado, etc), registra o erro e continua a cadeia de filtros sem autenticar o usuário
            log.error("Erro ao extrair email do token: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // - Passo 5: verificar se o email foi extraído e se ainda não há um usuário autenticado no contexto de segurança - //
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
            try {
                // - Passo 6: carregar os detalhes do usuário a partir do email extraído - //
                UserDetails userDetails = userDetailsService.loadUserByUsername(email); // Usa o UserDetailsService para carregar os detalhes do usuário a partir do email

                // - Passo 7: validar o token usando os detalhes do usuário - //
                if (jwtUtil.validarToken(token, userDetails)) { // Verifica se o token é válido para o usuário carregado
                    // - Passo 8: criar um objeto de autenticação e configurar o contexto de segurança - //
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities() // Cria um token de autenticação com os detalhes do usuário e suas autoridades (perfis)
                    );
                    
                    // - Passo 9: adicionar detalhes da requisição ao token de autenticação e configurar o contexto de segurança - //
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Configura os detalhes da autenticação com as informações da requisição (IP, sessão, etc)
                    
                    // - Passo 10: definir o token de autenticação no contexto de segurança para que o Spring reconheça o usuário como autenticado - //
                    SecurityContextHolder.getContext().setAuthentication(authToken); // Define o token de autenticação no contexto de segurança para que o Spring reconheça o usuário como autenticado
                    
                    log.debug("Usuário autenticado: {}", email); // Registra um log de depuração indicando que o usuário foi autenticado com sucesso
                }
            } catch (Exception e) {
                // Se o usuário não for encontrado ou houver outro erro, apenas registra e continua
                // Isso é importante para endpoints públicos como registro
                log.debug("Não foi possível autenticar usuário com email: {} - Erro: {}", email, e.getMessage());
            }
        }

        // - Passo 11: continuar a cadeia de filtros - //
        filterChain.doFilter(request, response); // Continua a cadeia de filtros para que a requisição seja processada normalmente, mesmo que o usuário não tenha sido autenticado (por exemplo, para acessar endpoints públicos)

    }
}