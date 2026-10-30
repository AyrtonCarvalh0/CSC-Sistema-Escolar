package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.DTO.portal.*;
import com.eiba.System_Finances.Enums.Role;
import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Usuario;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.AvisoRepository;
import com.eiba.System_Finances.repository.BoletimRepository;
import com.eiba.System_Finances.repository.PagamentoRepository;
import com.eiba.System_Finances.service.PortalAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/portal")
public class PortalController {

    @Autowired PortalAuthService portalAuthService;
    @Autowired AlunoRepository alunoRepository;
    @Autowired PagamentoRepository pagamentoRepository;
    @Autowired AvisoRepository avisoRepository;
    @Autowired BoletimRepository boletimRepository;

    // ─── AUTH ────────────────────────────────────────────────────────────────

    @PostMapping("/login")
    public ResponseEntity<PortalLoginResponseDTO> login(@RequestBody PortalLoginRequestDTO dto) {
        String token = portalAuthService.autenticar(dto.cpf(), dto.senha());
        if (token == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(new PortalLoginResponseDTO(token));
    }

    // ─── DADOS DO ALUNO ──────────────────────────────────────────────────────

    @GetMapping("/meus-dados")
    public ResponseEntity<MeusDadosDTO> meusDados(@AuthenticationPrincipal Aluno aluno) {
        String turma = aluno.getTurma() != null ? aluno.getTurma().getNome() : null;
        String responsavel = aluno.getResponsavel() != null ? aluno.getResponsavel().getName() : null;

        return ResponseEntity.ok(new MeusDadosDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getCpf(),
                aluno.getData_nascimento(),
                turma,
                responsavel
        ));
    }

    // ─── PENDÊNCIAS ──────────────────────────────────────────────────────────

    @GetMapping("/pendencias")
    public ResponseEntity<List<PendenciaDTO>> pendencias(@AuthenticationPrincipal Aluno aluno) {
        List<PendenciaDTO> pendencias = pagamentoRepository
                .findByAlunoAndPago(aluno, false)
                .stream()
                .map(p -> new PendenciaDTO(p.getId(), p.getMes(), p.getValor()))
                .toList();

        return ResponseEntity.ok(pendencias);
    }

    // ─── AVISOS ──────────────────────────────────────────────────────────────

    @GetMapping("/avisos")
    public ResponseEntity<List<AvisoDTO>> avisos(@AuthenticationPrincipal Aluno aluno) {
        UUID turmaId = aluno.getTurma() != null ? aluno.getTurma().getId() : null;

        List<AvisoDTO> avisos = avisoRepository
                .findAvisosVisiveis(aluno.getId(), turmaId, LocalDateTime.now())
                .stream()
                .map(a -> new AvisoDTO(a.getId(),
                    a.getTitulo(),
                    a.getConteudo(),
                    a.getCriadoEm(),
                    a.getExpiraEm(),
                    resolverAutor(a.getCriadoPor())
                ))
                .toList();

        return ResponseEntity.ok(avisos);
    }

    private String resolverAutor(Usuario usuario) {
        if (usuario == null) return null;
        return usuario.getRole() == Role.ADMIN ? "Diretoria" : "Secretaria";
    }

    // ─── BOLETIM ─────────────────────────────────────────────────────────────

    @GetMapping("/boletim")
    public ResponseEntity<List<BoletimItemDTO>> boletim(
            @AuthenticationPrincipal Aluno aluno,
            @RequestParam(required = false) Integer bimestre,
            @RequestParam(required = false) Integer ano
    ) {
        int anoConsultado = ano != null ? ano : LocalDateTime.now().getYear();

        List<BoletimItemDTO> notas;

        if (bimestre != null) {
            notas = boletimRepository
                    .findByAluno_IdAndBimestreAndAnoOrderByDisciplinaAsc(aluno.getId(), bimestre, anoConsultado)
                    .stream()
                    .map(b -> new BoletimItemDTO(b.getDisciplina(), b.getNota(), b.getBimestre(), b.getAno()))
                    .toList();
        } else {
            notas = boletimRepository
                    .findByAluno_IdAndAnoOrderByBimestreAscDisciplinaAsc(aluno.getId(), anoConsultado)
                    .stream()
                    .map(b -> new BoletimItemDTO(b.getDisciplina(), b.getNota(), b.getBimestre(), b.getAno()))
                    .toList();
        }

        return ResponseEntity.ok(notas);
    }
}
