package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.Boletim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoletimRepository extends JpaRepository<Boletim, UUID> {

    List<Boletim> findByAluno_IdAndBimestreAndAnoOrderByDisciplinaAsc(
            UUID alunoId, Integer bimestre, Integer ano
    );

    List<Boletim> findByAluno_IdAndAnoOrderByBimestreAscDisciplinaAsc(
            UUID alunoId, Integer ano
    );
}
