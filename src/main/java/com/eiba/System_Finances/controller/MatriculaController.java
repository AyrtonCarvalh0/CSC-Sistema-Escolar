package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.entity.Matricula;
import com.eiba.System_Finances.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @PostMapping
    public Matricula matricularAluno(@RequestBody Matricula matricula) {
        return matriculaService.matricularAluno(matricula);
    }

    @GetMapping("/{id}")
    public Matricula buscarMatriculaById(@PathVariable UUID id) {
        return matriculaService.bucarMatricula(id);
    }

    @GetMapping
    public List<Matricula> listarTodasMatriculas() {
        return matriculaService.listarTodasMatriculas();
    }

    @GetMapping("/turma/{turmaId}")
    public List<Matricula> listarMatriculasDaTurma(@PathVariable UUID turmaId) {
        return matriculaService.listarMatriculasDaTurma(turmaId);
    }

    @DeleteMapping("/{id}")
    public void cancelarMatricula(@PathVariable UUID id) {
        matriculaService.cancelarMatricula(id);
    }
}
