package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.Aluno;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends MongoRepository<Aluno,String> {
    List<Aluno> findByResponsavelId(String responsavelId);
    Optional<Aluno> findByCpf(String cpf);
    List<Aluno> findByNome(String alunoNome);
}
