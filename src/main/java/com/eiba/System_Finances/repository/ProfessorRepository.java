package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, UUID> {
}
