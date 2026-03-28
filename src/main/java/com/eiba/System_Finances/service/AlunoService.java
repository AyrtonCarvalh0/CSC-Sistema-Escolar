package com.eiba.System_Finances.service;

import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Responsavel;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;
    
    public Aluno dadosAluno(String id, String nome, String cpf, String responsavelId, Date data_nascimento){
        Aluno aluno = new Aluno();
        aluno.setId(id);
        aluno.setnome(nome);
        aluno.setCpf(cpf);
        aluno.setResponsavelId(responsavelId);
        aluno.setData_nascimento(data_nascimento);
        return aluno;
    }

    public Aluno cadastrarAluno(Aluno aluno){
       //Evitar duplicidade de cadastro
       if(aluno.getId() != null && alunoRepository.existsById(aluno.getId())){
           throw new RuntimeException("Erro: Este aluno já está cadastrado no sistema");
       }
        return alunoRepository.save(aluno);

    }
    public List<Aluno> listarTodosOsAlunos(){
        return alunoRepository.findAll();
    }

    public Aluno buscarAlunoPorCPF (String cpf){
        if(!alunoRepository.existsById(cpf)){
            throw new RuntimeException("Id não encontrado");
        }
        return alunoRepository.findById(cpf).orElse(null);
    }
    
    public Aluno buscarAlunoPorNome (String nome){
        List<Aluno> alunos = alunoRepository.findAll();
        for(Aluno aluno : alunos){
            if(aluno.getnome().equalsIgnoreCase(nome)){
                return aluno;
            }
        }
        throw new RuntimeException("Aluno não encontrado");
    }
    public void deletarAlunoporCPF (String cpf){

        alunoRepository.deleteById(cpf);
    }
    
    public Object buscarDadosCompletos(String id){
        // Busca o aluno pelos dados básicos
        Aluno aluno = buscarAlunoPorCPF(id);
        
        // Aqui você pode adicionar lógica para buscar dados relacionados:
        // - Responsavel do aluno
        // - Matrículas
        // - Turma
        // - Pagamentos
        // Por enquanto, retorna o aluno com todos seus dados
        
        return aluno;
    }
}
