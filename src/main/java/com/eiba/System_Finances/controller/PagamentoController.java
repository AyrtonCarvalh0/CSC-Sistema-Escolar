package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.DTO.DevedorDTO;
import com.eiba.System_Finances.DTO.ResumoCaixaDTO;
import com.eiba.System_Finances.entity.Pagamento;
import com.eiba.System_Finances.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping
    public Pagamento registrarPagamento(@RequestBody Pagamento pagamento) {
        return pagamentoService.cadastrarPagamento(pagamento);
    }

    @GetMapping
    public List<Pagamento> listarTodosPagamentos() {
        return pagamentoService.listarPagamentos();
    }

    @GetMapping("/pendentes/{alunoCPF}")
    public List<Pagamento> listarPendentes(@PathVariable String alunoCPF) {
        return pagamentoService.ListarPendentes(alunoCPF);
    }

    @GetMapping("/devedores")
    public ResponseEntity<List<DevedorDTO>> buscarDevedores() {
        return ResponseEntity.ok(pagamentoService.listarDevedoresComNome());
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<Pagamento> confirmarPagamento(@PathVariable UUID id) {
        return ResponseEntity.ok(pagamentoService.darBaixaPagamento(id));
    }

    @GetMapping("/devedores/busca")
    public ResponseEntity<List<DevedorDTO>> buscarDevedoresPorMes(@RequestParam String mes) {
        return ResponseEntity.ok(pagamentoService.listarDevedoresPorMes(mes));
    }

    @GetMapping("/{id}/recibo")
    public ResponseEntity<String> obterRecibo(@PathVariable UUID id) {
        return ResponseEntity.ok(pagamentoService.gerarRecibo(id));
    }

    @PostMapping("/gerar-mes")
    public ResponseEntity<String> gerarMes(@RequestParam String mes) {
        String resultado = pagamentoService.gerarMensalidadesDoMes(mes);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/resumo")
    public ResponseEntity<ResumoCaixaDTO> obterResumo(@RequestParam String mes) {
        return ResponseEntity.ok(pagamentoService.gerarResumoDoMes(mes));
    }

    @GetMapping("/devedores/turma")
    public ResponseEntity<List<DevedorDTO>> devedoresPorTurma(
            @RequestParam String mes,
            @RequestParam UUID turmaId) {
        return ResponseEntity.ok(pagamentoService.listarDevedoresPorMesETurma(mes, turmaId));
    }
}
