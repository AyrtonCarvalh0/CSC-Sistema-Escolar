package com.eiba.System_Finances.service;

import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Matricula;
import com.eiba.System_Finances.entity.Turma;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.MatriculaRepository;
import com.eiba.System_Finances.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    public Matricula matricularAluno(Matricula matricula) {
        return matriculaRepository.save(matricula);
    }

    public Matricula bucarMatricula(UUID id) {
        if (!matriculaRepository.existsById(id)) {
            throw new RuntimeException("Id não encontrado");
        }
        return matriculaRepository.findById(id).orElse(null);
    }

    public List<Matricula> listarTodasMatriculas() {
        return matriculaRepository.findAll();
    }

    public List<Matricula> listarMatriculasDaTurma(UUID turmaID) {
        return matriculaRepository.findByTurma_Id(turmaID);
    }

    public void cancelarMatricula(UUID id) {
        if (!matriculaRepository.existsById(id)) {
            throw new RuntimeException("Id não encontrado");
        }
        matriculaRepository.deleteById(id);
    }
}
