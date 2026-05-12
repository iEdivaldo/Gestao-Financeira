package com.gestaofinanceira.backend.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "logs_auditorias")
@Data
@Builder
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String acao;

    @Column(nullable = true, length = 50)
    private String entidade;

    @Column(nullable = true)
    private UUID entidade_id;

    @Column(columnDefinition = "TEXT")
    private String detalhe;

    @Column(length = 45)
    private String ip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

}