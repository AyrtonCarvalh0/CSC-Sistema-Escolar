package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AvisoRepository extends JpaRepository<Aviso, UUID> {

    /**
     * Busca avisos visíveis para um aluno:
     * - Avisos gerais (turma_id e aluno_id nulos)
     * - Avisos da turma do aluno
     * - Avisos específicos para o aluno
     */
    @Query("""
            SELECT a FROM Aviso a
            WHERE a.expiraEm > :agora
              AND (
                (a.turma IS NULL AND a.aluno IS NULL)
                OR a.turma.id = :turmaId
                OR a.aluno.id = :alunoId
              )
            ORDER BY a.criadoEm DESC
            """)
    List<Aviso> findAvisosVisiveis(
            @Param("alunoId") UUID alunoId,
            @Param("turmaId") UUID turmaId,
            @Param("agora") LocalDateTime agora
    );
}
