package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.entity.AuditLog;
import com.eiba.System_Finances.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:5173"})
public class AuditController {

    @Autowired
    private AuditService auditService;

    @GetMapping
    public ResponseEntity<List<AuditLog>> listar() {
        return ResponseEntity.ok(auditService.listarRecentes());
    }

    @GetMapping("/usuario/{login}")
    public ResponseEntity<List<AuditLog>> porUsuario(@PathVariable String login) {
        return ResponseEntity.ok(auditService.listarPorUsuario(login));
    }
}
