package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.Matrícula;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends MongoRepository<Matrícula,String> {
    List<Matrícula> findByAlunoId(String alunoId);

    List<Matrícula> findByTurmaId(String turmaId);
}
