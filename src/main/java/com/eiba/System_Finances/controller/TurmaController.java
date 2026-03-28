package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.entity.Turma;
import com.eiba.System_Finances.service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turmas")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @PostMapping
    public Turma criarTurma(@RequestBody Turma turma){
        return turmaService.criarTurma(turma);
    }

    @GetMapping("/{id}")
    public Turma buscarTurmaById(@PathVariable String id){
        return turmaService.findById(id);
    }

    @GetMapping
    public List<Turma> listarTodasTurmas(){
        return turmaService.listarTodos();
    }

    @PutMapping("/{id}")
    public Turma atualizarTurma(@PathVariable String id, @RequestBody Turma turma){
        turma.setId(id);
        return turmaService.atualizarTurma(turma);
    }

    @DeleteMapping("/{id}")
    public void deletarTurma(@PathVariable String id){
        turmaService.deletarTurma(id);
    }
}



