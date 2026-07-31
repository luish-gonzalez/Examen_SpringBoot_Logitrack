package com.logitrack.examen;

import com.logitrack.entities.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExamenAuditoriaRepository
        extends JpaRepository<Auditoria, Long> {

    @Query("""
            SELECT auditoria
            FROM Auditoria auditoria
            WHERE (
                :productoId IS NULL
                OR (
                    auditoria.entidadAfectada = 'Producto'
                    AND auditoria.entidadId = :productoId
                )
            )
            AND (
                :fechaInicio IS NULL
                OR auditoria.fechaHora >= :fechaInicio
            )
            AND (
                :fechaFin IS NULL
                OR auditoria.fechaHora <= :fechaFin
            )
            AND (
                :campoModificado IS NULL
                OR TRIM(:campoModificado) = ''
                OR LOWER(COALESCE(auditoria.valoresAnteriores, ''))
                    LIKE LOWER(CONCAT('%', :campoModificado, '%'))
                OR LOWER(COALESCE(auditoria.valoresNuevos, ''))
                    LIKE LOWER(CONCAT('%', :campoModificado, '%'))
            )
            ORDER BY auditoria.fechaHora DESC
            """)
    List<Auditoria> buscarAuditoriasConFiltros(
            @Param("productoId") Long productoId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("campoModificado") String campoModificado
    );
}