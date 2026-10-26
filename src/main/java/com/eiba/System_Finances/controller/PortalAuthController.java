package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.DTO.LoginPortalRequestDTO;
import com.eiba.System_Finances.DTO.LoginResponseDTO;
import com.eiba.System_Finances.service.PortalAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portal")
public class PortalAuthController {
    @Autowired
    PortalAuthService portalAuthService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginPortalRequestDTO dto) {

        String token = portalAuthService.autenticar(dto.cpf(), dto.senha());

        if (token == null) {
            return ResponseEntity.status(401).body("CPF ou senha inválidos");
        }

        return ResponseEntity.ok(new LoginResponseDTO(token, dto.cpf(), "ALUNO"));
    }
}
