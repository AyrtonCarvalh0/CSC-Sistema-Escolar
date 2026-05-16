package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.DTO.CadastroAlunoDTO;
import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Responsavel;
import com.eiba.System_Finances.entity.Turma;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.TurmaRepository;
import com.eiba.System_Finances.service.AlunoService;
import com.eiba.System_Finances.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

    @Autowired private AlunoService alunoService;
    @Autowired private TurmaRepository turmaRepository;
    @Autowired private AlunoRepository alunoRepository;
    @Autowired private AuditService auditService;

    private String getUsuarioLogado(UserDetails user) {
        return user != null ? user.getUsername() : "sistema";
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return ip != null ? ip.split(",")[0] : request.getRemoteAddr();
    }

    @PostMapping
    public ResponseEntity<Aluno> criar(@Valid @RequestBody CadastroAlunoDTO dto,
                       @AuthenticationPrincipal UserDetails userDetails,
                       HttpServletRequest request) {
        Aluno aluno = new Aluno();
        aluno.setNome(dto.nome());
        aluno.setCpf(dto.cpf());
        if (dto.dataNascimento() != null) {
            aluno.setData_nascimento(LocalDate.parse(dto.dataNascimento()));
        }
        if (dto.responsavelId() != null) {
            Responsavel resp = new Responsavel();
            resp.setId(dto.responsavelId());
            aluno.setResponsavel(resp);
        }
        if (dto.turmaId() != null) {
            Turma turma = new Turma();
            turma.setId(dto.turmaId());
            aluno.setTurma(turma);
        }
        Aluno saved = alunoService.cadastrarAluno(aluno);
        auditService.registrar(
            getUsuarioLogado(userDetails),
            "CRIAR_ALUNO",
            "ALUNO",
            "Aluno criado: " + saved.getNome() + " | CPF: " + saved.getCpf(),
            getIp(request)
        );
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Aluno>> listarTodos() {
        return ResponseEntity.ok(alunoService.listarTodosOsAlunos());
    }

    @GetMapping("/cpf/{cpf}")
    public Aluno buscarPorCpf(@PathVariable String cpf) {
        return alunoService.buscarAlunoPorCPF(cpf);
    }

    @GetMapping("/ficha/{cpf}")
    public Object buscarCompleto(@PathVariable("cpf") String cpf) {
        return alunoService.buscarFichaCompleta(cpf);
    }

    @GetMapping("/buscar-nome")
    public ResponseEntity<List<Aluno>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(alunoService.buscarPorParteDoNome(nome));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable UUID id,
                                                  @RequestBody Aluno aluno,
                                                  @AuthenticationPrincipal UserDetails userDetails,
                                                  HttpServletRequest request) {
        Aluno updated = alunoService.atualizarAluno(id, aluno);
        auditService.registrar(
            getUsuarioLogado(userDetails),
            "EDITAR_ALUNO",
            "ALUNO",
            "Aluno atualizado: ID " + id,
            getIp(request)
        );
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAluno(@PathVariable UUID id,
                                              @AuthenticationPrincipal UserDetails userDetails,
                                              HttpServletRequest request) {
        alunoService.excluirAluno(id);
        auditService.registrar(
            getUsuarioLogado(userDetails),
            "DELETAR_ALUNO",
            "ALUNO",
            "Aluno removido: ID " + id,
            getIp(request)
        );
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
