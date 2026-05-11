package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.entity.Matrícula;
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
    public Matrícula matricularAluno(@RequestBody Matrícula matrícula) {
        return matriculaService.matricularAluno(matrícula);
    }

    @GetMapping("/{id}")
    public Matrícula buscarMatriculaById(@PathVariable UUID id) {
        return matriculaService.bucarMatricula(id);
    }

    @GetMapping
    public List<Matrícula> listarTodasMatriculas() {
        return matriculaService.listarTodasMatriculas();
    }

    @GetMapping("/turma/{turmaId}")
    public List<Matrícula> listarMatriculasDaTurma(@PathVariable UUID turmaId) {
        return matriculaService.listarMatriculasDaTurma(turmaId);
    }

    @DeleteMapping("/{id}")
    public void cancelarMatricula(@PathVariable UUID id) {
        matriculaService.cancelarMatricula(id);
    }
}
