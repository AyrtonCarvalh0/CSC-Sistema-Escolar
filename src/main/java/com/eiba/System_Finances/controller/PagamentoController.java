package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.entity.Pagamento;
import com.eiba.System_Finances.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping
    public Pagamento registrarPagamento(@RequestBody Pagamento pagamento){
        return pagamentoService.cadastrarPagamento(pagamento);
    }

    @GetMapping
    public List<Pagamento> listarTodosPagamentos(){
        return pagamentoService.listarPagamentos();
    }

    @GetMapping("/pendentes/{alunoCPF}")
    public List<Pagamento> listarPendentes(@PathVariable String alunoCPF){
        return pagamentoService.ListarPendentes(alunoCPF);
    }

    @GetMapping("/devedores")
    public List<Pagamento> buscarDevedores() {
        return pagamentoService.listarDevedores();
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<Pagamento> confirmarPagamento(@PathVariable String id) {
        return ResponseEntity.ok(pagamentoService.darBaixaPagamento(id));
    }

}


