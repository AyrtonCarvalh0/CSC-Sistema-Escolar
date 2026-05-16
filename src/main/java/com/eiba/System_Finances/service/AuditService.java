package com.eiba.System_Finances.service;

import com.eiba.System_Finances.entity.AuditLog;
import com.eiba.System_Finances.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void registrar(String usuario, String acao, String entidade, String detalhe, String ip) {
        auditLogRepository.save(new AuditLog(usuario, acao, entidade, detalhe, ip));
    }

    public List<AuditLog> listarRecentes() {
        return auditLogRepository.findTop100ByOrderByDataHoraDesc();
    }

    public List<AuditLog> listarPorUsuario(String usuario) {
        return auditLogRepository.findByUsuarioOrderByDataHoraDesc(usuario);
    }
}
