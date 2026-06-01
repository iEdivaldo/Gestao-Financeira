package com.gestaofinanceira.backend.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice // intercepta todos exceções dos controllers e retorna json
@Slf4j
public class GlobalExceptionHandler {

    private record ErroResponse(
        int status,
        String erro,
        String mensagem,
        LocalDateTime timestamp
    ) {}


    // Erros de validação (@Valid dos controllers)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidacao(MethodArgumentNotValidException ex) {
        // Coleta todos os erros do campo em um mnap para retorna o email e a mensagem de erro
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField();
            String mensagem = error.getDefaultMessage();
            erros.put(campo, mensagem);
        });

        // HTTP 400 Bad Request - dados enviados são invalidos
        return ResponseEntity.badRequest().body(erros);
    }

    // Erros de recurso não encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErroResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ErroResponse(404, "Não encontrado", ex.getMessage(), LocalDateTime.now())
        );
    }

    // Erros de regras de negócios violada
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErroResponse> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
            new ErroResponse(422, "Regra de negócio violada", ex.getMessage(), LocalDateTime.now())
        );
    }

    // Erros de credenciais inválidas
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            new ErroResponse(401, "Não autorizado", "Email ou senha estão incorretos", LocalDateTime.now())
        );
    }

    // Erros de acesso negado (usuário sem permissão)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            new ErroResponse(403, "Acesso negado", "Você não tem permissão para acessar este recurso", LocalDateTime.now())
        );
    }

    // Erros genéricos (qualquer outra exceção não tratada)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGeneric(Exception ex) {
        log.error("Erro inesperado: ", ex); // Loga o erro para análise posterior
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            new ErroResponse(500, "Erro interno", "Ocorreu um erro inesperado. Tente novamente mais tarde.", LocalDateTime.now())
        );
    }
}