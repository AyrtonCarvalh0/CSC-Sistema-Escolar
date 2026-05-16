package com.eiba.System_Finances.service;

import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Matricula;
import com.eiba.System_Finances.entity.Professor;
import com.eiba.System_Finances.entity.Turma;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.MatriculaRepository;
import com.eiba.System_Finances.repository.ProfessorRepository;
import com.eiba.System_Finances.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    public Turma criarTurma(Turma turma) {
        if (turma.getProfessor() != null && turma.getProfessor().getId() != null) {
            Professor professor = professorRepository.findById(turma.getProfessor().getId())
                    .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
            turma.setProfessor(professor);
        }
        return turmaRepository.save(turma);
    }

    public Turma findById(UUID id) {
        if (!turmaRepository.existsById(id)) {
            throw new RuntimeException("Id não encontrado");
        }
        return turmaRepository.findById(id).orElse(null);
    }

    public List<Turma> listarTodos() {
        return turmaRepository.findAll();
    }

    public List<Aluno> listarAlunosDaTurma(UUID turmaId) {
        List<Matricula> matriculas = matriculaRepository.findByTurma_Id(turmaId);
        List<Aluno> alunos = new ArrayList<>();
        for (Matricula matricula : matriculas) {
            alunos.add(matricula.getAluno());
        }
        return alunos;
    }

    public List<Matricula> listarMatriculasDaTurma(UUID turmaId) {
        return matriculaRepository.findByTurma_Id(turmaId);
    }

    public Turma atualizarTurma(Turma turma) {
        if (!turmaRepository.existsById(turma.getId())) {
            throw new RuntimeException("Turma não encontrada");
        }
        return turmaRepository.save(turma);
    }

    public void deletarTurma(UUID id) {
        if (!turmaRepository.existsById(id)) {
            throw new RuntimeException("Turma não encontrada");
        }
        turmaRepository.deleteById(id);
    }
}
