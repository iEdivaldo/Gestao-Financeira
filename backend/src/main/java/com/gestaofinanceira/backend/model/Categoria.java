package com.gestaofinanceira.backend.model;

import java.util.UUID;

import com.gestaofinanceira.backend.model.ClassEnum.TipoTransacao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "categorias")
@Data
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(nullable = false, length = 10)
    private TipoTransacao tipo;

    @Column(nullable = false, length = 7)
    private String cor;

    @Column(nullable = false, length = 50)
    private String icone;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
}
