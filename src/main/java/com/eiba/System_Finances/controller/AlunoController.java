package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

        @Autowired
        private AlunoService alunoService;

        @PostMapping
        public Aluno criar(@RequestBody Aluno aluno){
            return alunoService.cadastrarAluno(aluno);
        }

        @GetMapping("/cpf/{cpf}")
        public Aluno buscarPorCpf(@PathVariable String cpf){
            return alunoService.buscarAlunoPorCPF(cpf);
        }

        @GetMapping("/nome/{nome}")
        public List<Aluno> buscarPorNome(@PathVariable String nome){
            return Collections.singletonList(alunoService.buscarAlunoPorNome(nome));
        }

        @GetMapping("/{id}/completo")
        public Object buscarCompleto(@PathVariable String id){
            // Retorna aluno com todos seus dados básicos + dados relacionados
            return alunoService.buscarFichaCompleta(id);
        }
    }

