package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, UUID> {
    List<Aluno> findByResponsavel_Id(UUID responsavelId);
    Optional<Aluno> findByCpf(String cpf);
    List<Aluno> findByNome(String alunoNome);
    List<Aluno> findByNomeContainingIgnoreCase(String nome);
    List<Aluno> findByTurma(Turma turma);
    List<Aluno> findByTurma_Id(UUID turmaId);
}
