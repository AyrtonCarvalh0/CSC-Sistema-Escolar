package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    List<AuditLog> findTop100ByOrderByDataHoraDesc();
    List<AuditLog> findByUsuarioOrderByDataHoraDesc(String usuario);
    List<AuditLog> findByEntidadeOrderByDataHoraDesc(String entidade);
}
