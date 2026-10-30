package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.DTO.CriarAvisoRequestDTO;
import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Aviso;
import com.eiba.System_Finances.entity.Turma;
import com.eiba.System_Finances.entity.Usuario;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.AvisoRepository;
import com.eiba.System_Finances.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/avisos")
public class AvisoController {

    @Autowired AvisoRepository avisoRepository;
    @Autowired TurmaRepository turmaRepository;
    @Autowired AlunoRepository alunoRepository;

    @PostMapping
    public ResponseEntity<?> criar(
            @AuthenticationPrincipal Usuario usuario,
            @RequestBody CriarAvisoRequestDTO dto
    ) {
        if (dto.titulo() == null || dto.titulo().isBlank()) {
            return ResponseEntity.badRequest().body("O título é obrigatório.");
        }
        if (dto.conteudo() == null || dto.conteudo().isBlank()) {
            return ResponseEntity.badRequest().body("O conteúdo é obrigatório.");
        }
        if (dto.expiraEm() == null || dto.expiraEm().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("A data de expiração deve ser uma data futura.");
        }
        if (dto.turmaId() != null && dto.alunoId() != null) {
            return ResponseEntity.badRequest().body("Informe apenas turmaId ou alunoId, não os dois.");
        }

        Aviso aviso = new Aviso();
        aviso.setTitulo(dto.titulo());
        aviso.setConteudo(dto.conteudo());
        aviso.setCriadoEm(LocalDateTime.now());
        aviso.setExpiraEm(dto.expiraEm());
        aviso.setCriadoPor(usuario);

        if (dto.turmaId() != null) {
            Turma turma = turmaRepository.findById(dto.turmaId()).orElse(null);
            if (turma == null) {
                return ResponseEntity.badRequest().body("Turma não encontrada.");
            }
            aviso.setTurma(turma);
        }

        if (dto.alunoId() != null) {
            Aluno aluno = alunoRepository.findById(dto.alunoId()).orElse(null);
            if (aluno == null) {
                return ResponseEntity.badRequest().body("Aluno não encontrado.");
            }
            aviso.setAluno(aluno);
        }

        avisoRepository.save(aviso);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public ResponseEntity<List<Aviso>> listar() {
        return ResponseEntity.ok(avisoRepository.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        if (!avisoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        avisoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
