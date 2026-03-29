package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

        @GetMapping("/ficha/{cpf}")
        public Object buscarCompleto(@PathVariable("cpf") String cpf) { // O nome dentro das aspas deve ser igual ao da URL
            return alunoService.buscarFichaCompleta(cpf);
        }

        @GetMapping("/buscar-nome")
        public ResponseEntity<List<Aluno>> buscarPorNome(@RequestParam String nome) {
            return ResponseEntity.ok(alunoService.buscarPorParteDoNome(nome));
        }
}

