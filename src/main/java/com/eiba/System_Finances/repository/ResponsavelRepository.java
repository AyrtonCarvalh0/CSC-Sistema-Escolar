package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.Responsavel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsavelRepository extends MongoRepository<Responsavel,String> {
}
