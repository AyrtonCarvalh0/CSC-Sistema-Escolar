package com.eiba.System_Finances.service;

import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Matrícula;
import com.eiba.System_Finances.entity.Turma;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.MatriculaRepository;
import com.eiba.System_Finances.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    public Matrícula matricularAluno(Matrícula matrícula){
        return matriculaRepository.save(matrícula);
    }

    public Matrícula bucarMatricula(String id){
        if(!matriculaRepository.existsById(id)){
            throw new RuntimeException("Id não encontrado");
        }
       return matriculaRepository.findById(id).orElse(null);
    }

    public List<Matrícula> listarTodasMatriculas(){
        return matriculaRepository.findAll();
    }

    public List<Matrícula> listarMatriculasDaTurma(String turmaID){
        return matriculaRepository.findByTurmaId(turmaID);
    }

    public void cancelarMatricula(String id){
        if(matriculaRepository.findById(id) == null){
            throw new RuntimeException("Id não encontrado");
        }
        matriculaRepository.deleteById(id);
    }



}
