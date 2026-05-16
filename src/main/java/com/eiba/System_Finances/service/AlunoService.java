package com.eiba.System_Finances.service;

import com.eiba.System_Finances.DTO.FichaAlunoDTO;
import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Pagamento;
import com.eiba.System_Finances.entity.Responsavel;
import com.eiba.System_Finances.entity.Turma;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.MatriculaRepository;
import com.eiba.System_Finances.repository.PagamentoRepository;
import com.eiba.System_Finances.repository.ResponsavelRepository;
import com.eiba.System_Finances.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private ResponsavelRepository responsavel;
    @Autowired
    private TurmaRepository turmaRepository;
    @Autowired
    private PagamentoRepository pagamentoRepository;
    @Autowired
    private MatriculaRepository matriculaRepository;

    public Aluno dadosAluno(UUID id, String nome, String cpf, LocalDate data_nascimento) {
        Aluno aluno = new Aluno();
        aluno.setId(id);
        aluno.setNome(nome);
        aluno.setCpf(cpf);
        aluno.setData_nascimento(data_nascimento);
        return aluno;
    }

    public Aluno cadastrarAluno(Aluno aluno) {
        if (aluno.getId() != null && alunoRepository.existsById(aluno.getId())) {
            throw new RuntimeException("Erro: Este aluno já está cadastrado no sistema");
        }

        if (aluno.getResponsavel() != null && aluno.getResponsavel().getId() != null) {
            Responsavel resp = responsavel.findById(aluno.getResponsavel().getId())
                    .orElseThrow(() -> new RuntimeException("Responsável não encontrado"));
            aluno.setResponsavel(resp);
        }

        if (aluno.getTurma() != null && aluno.getTurma().getId() != null) {
            Turma turma = turmaRepository.findById(aluno.getTurma().getId())
                    .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
            aluno.setTurma(turma);
        }

        return alunoRepository.save(aluno);
    }

    public List<Aluno> listarTodosOsAlunos() {
        return alunoRepository.findAll();
    }

    public Aluno buscarAlunoPorCPF(String cpf) {
        return alunoRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com CPF: " + cpf));
    }

    public Aluno buscarAlunoPorNome(String nome) {
        List<Aluno> alunos = alunoRepository.findAll();
        for (Aluno aluno : alunos) {
            if (aluno.getNome().equalsIgnoreCase(nome)) {
                return aluno;
            }
        }
        throw new RuntimeException("Aluno não encontrado");
    }

    public void deletarAlunoporCPF(String cpf) {
        Aluno aluno = alunoRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com CPF: " + cpf));
        alunoRepository.delete(aluno);
    }

    public FichaAlunoDTO buscarFichaCompleta(String cpf) {
        Aluno aluno = alunoRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o CPF: " + cpf));

        Responsavel resp = aluno.getResponsavel();
        if (resp == null) throw new RuntimeException("Responsável não encontrado");

        List<Pagamento> pagamentos = pagamentoRepository.findByAluno_Id(aluno.getId());

        Double totalDevendo = pagamentos.stream()
                .filter(p -> !p.isPago())
                .filter(p -> p.getValor() != null)
                .mapToDouble(Pagamento::getValor)
                .sum();

        String turmaNome = (aluno.getTurma() != null) ? aluno.getTurma().getNome() : null;

        FichaAlunoDTO ficha = new FichaAlunoDTO(
                aluno.getNome(),
                turmaNome,
                resp.getName(),
                pagamentos
        );

        ficha.setValorTotalEmAberto(totalDevendo);

        return ficha;
    }

    public List<Aluno> buscarPorParteDoNome(String nome) {
        List<Aluno> alunos = alunoRepository.findByNomeContainingIgnoreCase(nome);

        if (alunos.isEmpty()) {
            throw new RuntimeException("Nenhum aluno encontrado com o nome: " + nome);
        }

        return alunos;
    }

    public Aluno atualizarAluno(UUID id, Aluno dados) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + id));
        aluno.setNome(dados.getNome());
        aluno.setCpf(dados.getCpf());
        aluno.setTurma(dados.getTurma());
        aluno.setData_nascimento(dados.getData_nascimento());
        aluno.setResponsavel(dados.getResponsavel());
        return alunoRepository.save(aluno);
    }

    @Transactional
    public void excluirAluno(UUID id) {
        Aluno aluno = alunoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        pagamentoRepository.deleteByAluno(aluno);
        matriculaRepository.deleteByAluno(aluno);
        alunoRepository.deleteById(id);
    }
}
