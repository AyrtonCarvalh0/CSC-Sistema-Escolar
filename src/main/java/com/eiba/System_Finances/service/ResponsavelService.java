package com.eiba.System_Finances.service;

import com.eiba.System_Finances.entity.Responsavel;
import com.eiba.System_Finances.repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ResponsavelService {

    @Autowired
    private ResponsavelRepository responsavelRepository;

    public Responsavel cadastrarResponsavel(Responsavel responsavel) {
        return responsavelRepository.save(responsavel);
    }

    public Responsavel buscarResponsavelPorCPF(String cpf) {
        return responsavelRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Responsável não encontrado com CPF: " + cpf));
    }

    public List<Responsavel> litarTodosResponsaveis() {
        return responsavelRepository.findAll();
    }

    public void deletarByCPF(String cpf) {
        Responsavel responsavel = responsavelRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Responsável não encontrado com CPF: " + cpf));
        responsavelRepository.delete(responsavel);
    }

    public Responsavel atualizarResponsavel(Responsavel responsavel) {
        if (!responsavelRepository.existsById(responsavel.getId())) {
            throw new RuntimeException("Responsavel não encontrado");
        }
        return responsavelRepository.save(responsavel);
    }

    public List<Responsavel> buscarPorNome(String nome) {
        return responsavelRepository.findByNameContainingIgnoreCase(nome);
    }

    public void deletarResponsavel(UUID id) {
        if (!responsavelRepository.existsById(id)) {
            throw new RuntimeException("Responsavel não encontrado");
        }
        responsavelRepository.deleteById(id);
    }
}
