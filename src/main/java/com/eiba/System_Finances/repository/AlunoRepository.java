package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.Aluno;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends MongoRepository<Aluno,String> {
    List<Aluno> findByResponsavelId(String responsavelId);
    List<Aluno> findByCpf(String alunoCpf);
    List<Aluno> findByName(String alunoName);
}
