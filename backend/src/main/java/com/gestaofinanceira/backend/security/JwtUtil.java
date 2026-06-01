package com.gestaofinanceira.backend.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.gestaofinanceira.backend.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component // @Component para que o Spring possa gerenciar esta classe como um bean - assim podemos injetá-la em outros lugares, como no filtro de autenticação
public class JwtUtil {

    @Value("${jwt.secret}") // Leitura do valor da chave secreta do arquivo de configuração (application.properties ou application.yml)
    private String secret;

    @Value("${jwt.expiration}") // Leitura do valor do tempo de expiração do token do arquivo de configuração
    private Long expiration;

    // - Método público principal - //

    public String gerarToken(UserDetails userDetails) {
        // Cria o mapa de claims extras que serão embutidos no token
        Map<String, Object> claims = new HashMap<>();

        // Adiciona o perfil do usuário no token para o frontend poder exibir a interface correta
        // Cast para Usuario porque UserDetails é uma interface genérica, e precisamos acessar o método getPerfil() que é específico da nossa classe Usuario
        if (userDetails instanceof Usuario usuario) {
            // Java 16+ pattern matching já faz o cast e a verificação de tipo em uma única linha, então podemos usar diretamente 'usuario' dentro do bloco if
            claims.put("perfil", usuario.getPerfil().name()); // Adiciona o perfil do usuário como uma claim no token
            claims.put("nome", usuario.getNome()); // Adiciona o nome do usuário como uma claim no token
            claims.put("id", usuario.getId().toString()); // Adiciona o ID do usuário como uma claim no token
        }
        return construirToken(claims, userDetails);
    }

    public boolean validarToken(String token, UserDetails userDetails) {
        // Extrai o e-mail do token e compara com o email do usuario passado como parâmetro, além de verificar se o token não expirou
        final String email = extrairEmail(token);

        // Só é valido se email bate e o token não está expirado
        return email.equals(userDetails.getUsername()) && !isTokenExpirado(token);
    }

    public String extrairEmail(String token) {
        return extrairClaim(token, Claims::getSubject); // O email do usuário é armazenado no campo "subject" do token, então usamos Claims::getSubject para extrair esse valor
    }

    // - Métodos privados auxiliares - //

    private String construirToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
            .claims(extraClaims) // Adiciona as claims extras ao token
            .subject(userDetails.getUsername()) // Define o "subject" do token como o email do usuário
            .issuedAt(new Date(System.currentTimeMillis())) // Define a data de emissão do token como o momento atual
            .expiration(new Date(System.currentTimeMillis() + expiration)) // Define a data de expiração do token com base no tempo configurado
            .signWith(getSigningKey()) // Assina o token com a chave secreta
            .compact(); // Compacta o token em uma string
    }

    private <T> T extrairClaim(String token, Function<Claims, T> claimsResolver) {
        // Extrai todas as claims do token e depois aplica a função de resolução para obter a claim específica desejada
        final Claims claims = extrairTodosClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extrairTodosClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey()) // Configura o parser para usar a chave de assinatura correta para verificar o token
            .build()
            .parseSignedClaims(token) // Faz o parse e lança uma exceção se o token for inválido ou tiver sido adulterado
            .getPayload(); // Retorna as claims do token
    }

    private boolean isTokenExpirado(String token) {
        // Compara a data de expiração do token com a data atual para determinar se o token já expirou
        return extrairClaim(token, Claims::getExpiration).before(new Date());
    }

    private SecretKey getSigningKey() {
        // Converte a string da chave secreta em um array de bytes e cria a chave criptográfica adequada
        // Keys.hmacShaKeyFor é um método utilitário do JJWT que cria uma chave HMAC a partir de um array de bytes, garantindo que a chave tenha o tamanho correto para o algoritmo de assinatura usado (HS256, HS384, HS512)
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8); // Converte a string da chave secreta em um array de bytes usando UTF-8
        return Keys.hmacShaKeyFor(keyBytes);
    }
}