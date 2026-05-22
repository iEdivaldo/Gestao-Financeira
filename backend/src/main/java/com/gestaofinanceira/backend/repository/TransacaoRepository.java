package com.gestaofinanceira.backend.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gestaofinanceira.backend.model.Transacao;
import com.gestaofinanceira.backend.model.enums.StatusTransacao;
import com.gestaofinanceira.backend.model.enums.TipoTransacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {
    List<Transacao> findAllByUsuarioIdAndDataVencimentoBetween(UUID usuarioId, Date inicio, Date fim);

    List<Transacao> findAllByUsuarioIdAndTipoAndDataVencimentoBetween(UUID usuarioId, TipoTransacao tipo, Date inicio, Date fim);

    List<Transacao> findAllByUsuarioIdAndCategoriaId(UUID usuarioId, UUID categoriaId);

    // Consulta personalizada para somar os valores das transações por tipo e período
    @Query("""
            SELECT COALESCE(SUM(t.valor), 0)
            FROM Transacao t
            WHERE t.usuario.id = :usuarioId
                AND t.tipo = :tipo
                AND t.status IN :statuses
                AND t.dataVencimento BETWEEN :inicio AND :fim
            """)
    BigDecimal somarPorTipoEPeriodo(
        @Param("usuarioId") UUID usuarioId,
        @Param("tipo") TipoTransacao tipo,
        @Param("statuses") List<StatusTransacao> statuses,
        @Param("inicio") LocalDate inicio,
        @Param("fim") LocalDate fim);

    // Consulta personalizada para somar os valores das transações por categoria e mês
    @Query("""
            SELECT COALESCE(SUM(t.valor), 0)
            FROM Transacao t
            WHERE t.usuario.id = :usuarioId
                AND t.categoria.id = :categoriaId
                AND t.tipo = 'DESPESA'
                and t.status = 'PAGO'
                and MONTH(t.dataVencimento) = :mes
                and YEAR(t.dataVencimento) = :ano
            """)
    BigDecimal somarGastoPorCategoriaEMes(
        @Param("usuarioId") UUID usuarioId,
        @Param("categoriaId") UUID categoriaId,
        @Param("mes") int mes,
        @Param("ano") int ano);

    Optional<Transacao> findByIdAndUsuarioId(UUID id, UUID usuarioId);
}
