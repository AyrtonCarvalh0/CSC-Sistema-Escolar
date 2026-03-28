package com.eiba.System_Finances.service;

import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Matrícula;
import com.eiba.System_Finances.entity.Turma;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.MatriculaRepository;
import com.eiba.System_Finances.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    public Turma criarTurma(Turma turma){
        return turmaRepository.save(turma);
    }
    public Turma findById(String id){
        if (!turmaRepository.existsById(id)) {
            throw new RuntimeException("Id não encontrado");
        }
        return turmaRepository.findById(id).orElse(null);
    }

    public List<Turma> listarTodos(){
        return turmaRepository.findAll();
    }
    public List<Aluno> listarAlunosDaTurma(String turmaId){

        List<Matrícula> matriculas = matriculaRepository.findByTurmaId(turmaId);

        List<Aluno> alunos = new ArrayList<>();

        for (Matrícula matricula : matriculas){

            alunoRepository.findById(matricula.getAlunoId())
                    .ifPresent(alunos::add);
        }
        return alunos;
    }
    public List<Matrícula> listarMatriculasDaTurma(String turmaId){
            return matriculaRepository.findByTurmaId(turmaId);
    }

    public Turma atualizarTurma(Turma turma){
        if(!turmaRepository.existsById(turma.getId())){
            throw new RuntimeException("Turma não encontrada");
        }
        return turmaRepository.save(turma);
    }

    public void deletarTurma(String id){
        if(!turmaRepository.existsById(id)){
            throw new RuntimeException("Turma não encontrada");
        }
        turmaRepository.deleteById(id);
    }
}
