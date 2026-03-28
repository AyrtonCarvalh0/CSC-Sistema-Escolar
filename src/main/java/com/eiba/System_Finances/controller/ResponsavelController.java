package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.entity.Responsavel;
import com.eiba.System_Finances.service.ResponsavelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/responsavel")
public class ResponsavelController {

    @Autowired
    private ResponsavelService responsavelService;

    @PostMapping
    public Responsavel cadastrarResponsavel(@RequestBody Responsavel Responsavel){
        return responsavelService.cadastrarResponsavel(Responsavel);
    }

    @GetMapping("/{cpf}")
    public Responsavel buscarResponsavelPorCPF(@PathVariable String cpf){
        return responsavelService.buscarResponsavelPorCPF(cpf);
    }

    @GetMapping
    public List<Responsavel> listarTodosResponsaveis(){
        return responsavelService.litarTodosResponsaveis();
    }

    @PutMapping("/{id}")
    public Responsavel atualizarResponsavel(@PathVariable String id, @RequestBody Responsavel Responsavel){
        Responsavel.setId(id);
        return responsavelService.atualizarResponsavel(Responsavel);
    }

    @DeleteMapping("/{id}")
    public void deletarResponsavel(@PathVariable String id){
        responsavelService.deletarResponsavel(id);
    }
}


