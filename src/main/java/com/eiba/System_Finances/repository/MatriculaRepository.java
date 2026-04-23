package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.Matrícula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatriculaRepository extends JpaRepository<Matrícula, UUID> {
    List<Matrícula> findByAluno_Id(UUID alunoId);
    List<Matrícula> findByTurma_Id(UUID turmaId);
}
