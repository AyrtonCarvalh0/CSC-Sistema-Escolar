package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {
    List<Pagamento> findByAluno_Id(UUID alunoId);
    List<Pagamento> findByAlunoAndPago(Aluno aluno, boolean pago);
    List<Pagamento> findByPagoFalse();
    boolean existsByAluno_IdAndMes(UUID alunoId, String mes);
    List<Pagamento> findByMesAndPagoFalse(String mes);
    boolean existsByAluno_IdAndPagoFalse(UUID alunoId);
    List<Pagamento> findByMes(String mes);
    boolean existsByMes(String mes);

    @Query("SELECT p FROM Pagamento p WHERE p.mes = :mes AND p.pago = false AND p.aluno.turma.id = :turmaId")
    List<Pagamento> findDevedoresByMesAndTurma(@Param("mes") String mes, @Param("turmaId") UUID turmaId);
}
