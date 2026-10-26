package com.eiba.System_Finances.service;

import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class PortalAuthService {

    @Autowired AlunoRepository alunoRepository;
    @Autowired TokenService tokenService;

    public String autenticar(String cpf, String senha) {

        // 1. Verifica se a senha tem o prefixo correto
        if (senha == null || !senha.startsWith("CSC@")) {
            return null;
        }

        // 2. Extrai a parte da data: "CSC@16062007" → "16062007"
        String parteData = senha.substring(4);

        // 3. Converte "16062007" para LocalDate 2007-06-16
        LocalDate dataNascimento;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
            dataNascimento = LocalDate.parse(parteData, formatter);
        } catch (DateTimeParseException e) {
            return null; // data inválida
        }

        // 4. Busca o aluno pelo CPF
        Aluno aluno = alunoRepository.findByCpf(cpf).orElse(null);
        if (aluno == null) {
            return null; // aluno não encontrado
        }

        // 5. Compara a data de nascimento
        if (!aluno.getData_nascimento().equals(dataNascimento)) {
            return null; // data não bate
        }

        // 6. Tudo ok → gera o token
        return tokenService.gerarTokenAluno(aluno);
    }
}
