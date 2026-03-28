package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.entity.Professor;
import com.eiba.System_Finances.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping
    public Professor cadastrarProfessor(@RequestBody Professor professor){
        return professorService.cadastrarProfessor(professor);
    }

    @GetMapping("/{id}")
    public Professor buscarProfessorById(@PathVariable String id){
        return professorService.buscarPorId(id);
    }

    @GetMapping
    public List<Professor> listarTodosProfessores(){
        return professorService.listarTodos();
    }

    @PutMapping("/{id}")
    public Professor atualizarProfessor(@PathVariable String id, @RequestBody Professor professor){
        professor.setId(id);
        return professorService.atualizarProfessor(professor);
    }

    @DeleteMapping("/{id}")
    public void deletarProfessor(@PathVariable String id){
        professorService.deletarById(id);
    }
}


