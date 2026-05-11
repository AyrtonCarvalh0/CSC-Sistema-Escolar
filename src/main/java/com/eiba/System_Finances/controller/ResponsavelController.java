package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.entity.Responsavel;
import com.eiba.System_Finances.service.ResponsavelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/responsavel")
public class ResponsavelController {

    @Autowired
    private ResponsavelService responsavelService;

    @PostMapping
    public Responsavel cadastrarResponsavel(@RequestBody Responsavel responsavel) {
        return responsavelService.cadastrarResponsavel(responsavel);
    }

    @GetMapping("/{cpf}")
    public Responsavel buscarResponsavelPorCPF(@PathVariable String cpf) {
        return responsavelService.buscarResponsavelPorCPF(cpf);
    }

    @GetMapping
    public List<Responsavel> listarTodosResponsaveis() {
        return responsavelService.litarTodosResponsaveis();
    }

    @PutMapping("/{id}")
    public Responsavel atualizarResponsavel(@PathVariable UUID id, @RequestBody Responsavel responsavel) {
        responsavel.setId(id);
        return responsavelService.atualizarResponsavel(responsavel);
    }

    @DeleteMapping("/{id}")
    public void deletarResponsavel(@PathVariable UUID id) {
        responsavelService.deletarResponsavel(id);
    }

    @GetMapping("/buscar-nome")
    public ResponseEntity<List<Responsavel>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(responsavelService.buscarPorNome(nome));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Responsavel> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(responsavelService.buscarResponsavelPorCPF(cpf));
    }
}
