package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.DTO.DevedorDTO;
import com.eiba.System_Finances.DTO.ResumoCaixaDTO;
import com.eiba.System_Finances.entity.Pagamento;
import com.eiba.System_Finances.repository.PagamentoRepository;
import com.eiba.System_Finances.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private PagamentoRepository pagamentoRepository;

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

    @GetMapping("/historico-mensal")
    public ResponseEntity<List<Map<String, Object>>> historicoMensal() {
        List<Map<String, Object>> historico = new ArrayList<>();
        LocalDate hoje = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/yyyy");

        for (int i = 5; i >= 0; i--) {
            String mesStr = hoje.minusMonths(i).format(fmt);
            List<Pagamento> pagamentos = pagamentoRepository.findByMes(mesStr);

            double recebido = pagamentos.stream()
                .filter(Pagamento::isPago)
                .mapToDouble(Pagamento::getValor)
                .sum();

            double pendente = pagamentos.stream()
                .filter(p -> !p.isPago())
                .mapToDouble(Pagamento::getValor)
                .sum();

            Map<String, Object> item = new HashMap<>();
            item.put("mes", mesStr);
            item.put("recebido", recebido);
            item.put("pendente", pendente);
            historico.add(item);
        }
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/proximos-vencimentos")
    public ResponseEntity<List<Map<String, Object>>> proximosVencimentos() {
        String mesAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy"));
        List<Pagamento> pendentes = pagamentoRepository.findByMesAndPagoFalse(mesAtual);

        List<Map<String, Object>> resultado = pendentes.stream()
            .map(p -> {
                Map<String, Object> item = new HashMap<>();
                item.put("nomeAluno", p.getAluno().getNome());
                item.put("turma", p.getAluno().getTurma() != null
                    ? p.getAluno().getTurma().getNome() : "Sem turma");
                item.put("valor", p.getValor());
                item.put("mes", p.getMes());
                return item;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }
}
