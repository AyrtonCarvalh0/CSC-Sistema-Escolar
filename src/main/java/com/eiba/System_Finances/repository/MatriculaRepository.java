package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, UUID> {
    List<Matricula> findByAluno_Id(UUID alunoId);
    List<Matricula> findByTurma_Id(UUID turmaId);
    void deleteByAluno(Aluno aluno);
}
