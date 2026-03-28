package com.eiba.System_Finances.service;

import DTO.FichaAlunoDTO;
import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Pagamento;
import com.eiba.System_Finances.entity.Responsavel;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.PagamentoRepository;
import com.eiba.System_Finances.repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private ResponsavelRepository responsavel;
    @Autowired
    private PagamentoRepository pagamentoRepository;
    
    public Aluno dadosAluno(String id, String nome, String cpf, String responsavelId, LocalDate data_nascimento){
        Aluno aluno = new Aluno();
        aluno.setId(id);
        aluno.setNome(nome);
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
            if(aluno.getNome().equalsIgnoreCase(nome)){
                return aluno;
            }
        }
        throw new RuntimeException("Aluno não encontrado");
    }
    public void deletarAlunoporCPF (String cpf){

        alunoRepository.deleteById(cpf);
    }

    public FichaAlunoDTO buscarFichaCompleta(String cpf) {
        // 1. Busca o aluno pelo CPF
        Aluno aluno = alunoRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o CPF: " + cpf));

        // 2. Busca os dados do responsável usando o ID que está no aluno
        Responsavel resp = responsavel.findById(aluno.getResponsavelId())
                .orElseThrow(() -> new RuntimeException("Responsável não encontrado"));
        
        // 3. Busca todos os pagamentos atrelados a esse aluno (usando o ID do Aluno, não do pai/mãe)
        List<Pagamento> pagamentos = pagamentoRepository.findByAlunoId(aluno.getId());

        // 4. Monta o pacotão (DTO) para enviar ao front-end
        return new FichaAlunoDTO(
                aluno.getNome(),
                aluno.getTurma(),
                resp.getName(),
                pagamentos
        );
    }
}
