package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, UUID> {
    List<Turma> findByProfessor_Id(UUID professorId);
}
