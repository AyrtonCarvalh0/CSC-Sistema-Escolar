package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.Turma;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurmaRepository extends MongoRepository<Turma,String> {
    List<Turma> findByProfessorId(String professorId);
}
