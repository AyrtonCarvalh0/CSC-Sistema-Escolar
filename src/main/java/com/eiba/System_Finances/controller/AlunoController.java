package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

        @Autowired
        private AlunoService alunoService;

        @PostMapping
        public Aluno criar(@RequestBody Aluno aluno){
            return alunoService.cadastrarAluno(aluno);
        }

        @GetMapping
        public ResponseEntity<List<Aluno>> listarTodos() {
            return ResponseEntity.ok(alunoService.listarTodosOsAlunos());
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

        @PutMapping("/{id}")
        public ResponseEntity<Aluno> atualizarAluno(@PathVariable UUID id, @RequestBody Aluno aluno) {
            return ResponseEntity.ok(alunoService.atualizarAluno(id, aluno));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> excluirAluno(@PathVariable UUID id) {
            alunoService.excluirAluno(id);
            return ResponseEntity.noContent().build();
        }
}

