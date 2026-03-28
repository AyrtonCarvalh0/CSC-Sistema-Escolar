package com.eiba.System_Finances.service;

import com.eiba.System_Finances.entity.Responsavel;
import com.eiba.System_Finances.repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResponsavelService {

    private Responsavel resp;

    @Autowired
    private ResponsavelRepository responsavelRepository;

    public Responsavel cadastrarResponsavel(Responsavel Responsavel){
        return responsavelRepository.save(Responsavel);
    }
    public Responsavel buscarResponsavelPorCPF(String cpf){

        if(!responsavelRepository.existsById(cpf)){
            throw new RuntimeException("Id não encontrado");

        }
        return responsavelRepository.findById(cpf).orElse(null);
    }
    public List<Responsavel> litarTodosResponsaveis(){
        return responsavelRepository.findAll();
    }
    public void deletarByCPF(String cpf){
        if(responsavelRepository.findById(cpf) == null){
            throw new RuntimeException("ID não encontrado");
        }
        responsavelRepository.deleteById(cpf);
    }

    public Responsavel atualizarResponsavel(Responsavel Responsavel){
        if(!responsavelRepository.existsById(Responsavel.getId())){
            throw new RuntimeException("Responsavel não encontrado");
        }
        return responsavelRepository.save(Responsavel);
    }

    public void deletarResponsavel(String id){
        if(!responsavelRepository.existsById(id)){
            throw new RuntimeException("Responsavel não encontrado");
        }
        responsavelRepository.deleteById(id);
    }
}
