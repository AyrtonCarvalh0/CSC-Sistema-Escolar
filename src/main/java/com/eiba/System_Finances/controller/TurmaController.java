package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Turma;
import com.eiba.System_Finances.service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/turmas")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @PostMapping
    public Turma criarTurma(@RequestBody Turma turma) {
        return turmaService.criarTurma(turma);
    }

    @GetMapping("/{id}")
    public Turma buscarTurmaById(@PathVariable UUID id) {
        return turmaService.findById(id);
    }

    @GetMapping
    public List<Turma> listarTodasTurmas() {
        return turmaService.listarTodos();
    }

    @PutMapping("/{id}")
    public Turma atualizarTurma(@PathVariable UUID id, @RequestBody Turma turma) {
        turma.setId(id);
        return turmaService.atualizarTurma(turma);
    }

    @DeleteMapping("/{id}")
    public void deletarTurma(@PathVariable UUID id) {
        turmaService.deletarTurma(id);
    }

    @GetMapping("/{id}/alunos")
    public ResponseEntity<List<Aluno>> listarAlunosDaTurma(@PathVariable UUID id) {
        return ResponseEntity.ok(turmaService.listarAlunosDaTurma(id));
    }
}
