package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.Responsavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResponsavelRepository extends JpaRepository<Responsavel, UUID> {
    Optional<Responsavel> findByCpf(String cpf);
    List<Responsavel> findByNameContainingIgnoreCase(String nome);
}
