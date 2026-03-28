package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.entity.Matrícula;
import com.eiba.System_Finances.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @PostMapping
    public Matrícula matricularAluno(@RequestBody Matrícula matrícula){
        return matriculaService.matricularAluno(matrícula);
    }

    @GetMapping("/{id}")
    public Matrícula buscarMatriculaById(@PathVariable String id){
        return matriculaService.bucarMatricula(id);
    }

    @GetMapping
    public List<Matrícula> listarTodasMatriculas(){
        return matriculaService.listarTodasMatriculas();
    }

    @GetMapping("/turma/{turmaId}")
    public List<Matrícula> listarMatriculasDaTurma(@PathVariable String turmaId){
        return matriculaService.listarMatriculasDaTurma(turmaId);
    }

    @DeleteMapping("/{id}")
    public void cancelarMatricula(@PathVariable String id){
        matriculaService.cancelarMatricula(id);
    }
}


