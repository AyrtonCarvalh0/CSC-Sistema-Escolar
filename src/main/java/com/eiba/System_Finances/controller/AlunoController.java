package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Turma;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.TurmaRepository;
import com.eiba.System_Finances.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

        @Autowired
        private AlunoService alunoService;

        @Autowired
        private TurmaRepository turmaRepository;

        @Autowired
        private AlunoRepository alunoRepository;

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

        @GetMapping("/por-turma")
        public ResponseEntity<List<Map<String, Object>>> alunosPorTurma() {
            List<Turma> turmas = turmaRepository.findAll();
            List<Map<String, Object>> resultado = new ArrayList<>();

            for (Turma turma : turmas) {
                List<Aluno> alunos = alunoRepository.findByTurma(turma);
                if (!alunos.isEmpty()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("turma", turma.getNome());
                    item.put("quantidade", alunos.size());
                    resultado.add(item);
                }
            }
            return ResponseEntity.ok(resultado);
        }
}

