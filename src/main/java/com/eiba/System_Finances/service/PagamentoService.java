package com.eiba.System_Finances.service;

import com.eiba.System_Finances.DTO.DevedorDTO;
import com.eiba.System_Finances.DTO.ResumoCaixaDTO;
import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Pagamento;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    public Pagamento cadastrarPagamento(Pagamento pagamento) {
        boolean jaExiste = pagamentoRepository.existsByAluno_IdAndMes(
                pagamento.getAluno().getId(),
                pagamento.getMes()
        );

        if (jaExiste) {
            throw new RuntimeException("Erro: Já existe um lançamento para o mês "
                    + pagamento.getMes() + " para este aluno.");
        }

        return pagamentoRepository.save(pagamento);
    }

    public List<Pagamento> listarPagamentos() {
        return pagamentoRepository.findAll();
    }

    public List<Pagamento> ListarPendentes(String alunoCPF) {
        Aluno aluno = alunoRepository.findByCpf(alunoCPF)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com CPF: " + alunoCPF));
        return pagamentoRepository.findByAlunoAndPago(aluno, false);
    }

    public List<DevedorDTO> listarDevedoresComNome() {
        List<Pagamento> devedores = pagamentoRepository.findByPagoFalse();

        return devedores.stream().map(pagamento -> {
            String nome = pagamento.getAluno() != null
                    ? pagamento.getAluno().getNome()
                    : "Aluno não identificado";
            return new DevedorDTO(nome, pagamento.getMes(), pagamento.getValor());
        }).toList();
    }

    public Pagamento darBaixaPagamento(UUID id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        Double valorAtualizado = calcularValorComAtraso(pagamento.getValor());
        pagamento.setValor(valorAtualizado);
        pagamento.setPago(true);
        pagamento.setDataPagamento(LocalDateTime.now());

        return pagamentoRepository.save(pagamento);
    }

    private Double calcularValorComAtraso(Double valorBase) {
        int diaAtual = LocalDate.now().getDayOfMonth();
        if (diaAtual > 10) {
            return valorBase + 20.00;
        } else if (diaAtual > 5) {
            return valorBase + 10.00;
        }
        return valorBase;
    }

    public List<DevedorDTO> listarDevedoresPorMes(String mes) {
        List<Pagamento> devedores = pagamentoRepository.findByMesAndPagoFalse(mes);

        return devedores.stream().map(pagamento -> {
            String nome = pagamento.getAluno() != null
                    ? pagamento.getAluno().getNome()
                    : "Aluno não encontrado";
            return new DevedorDTO(nome, pagamento.getMes(), pagamento.getValor());
        }).toList();
    }

    public String gerarRecibo(UUID pagamentoId) {
        Pagamento pag = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        Aluno aluno = pag.getAluno();
        if (aluno == null) throw new RuntimeException("Aluno não encontrado");

        return String.format(
                "--- RECIBO DE PAGAMENTO ---\n" +
                        "Aluno: %s\n" +
                        "Mês: %s\n" +
                        "Valor: R$ %.2f\n" +
                        "Status: QUITADO\n" +
                        "Data do Recebimento: %s\n" +
                        "---------------------------",
                aluno.getNome(), pag.getMes(), pag.getValor(),
                pag.getDataPagamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
    }

    public String gerarMensalidadesDoMes(String mesReferencia) {
        if (pagamentoRepository.existsByMes(mesReferencia)) {
            throw new RuntimeException("Erro: As mensalidades de "
                    + mesReferencia + " já foram geradas anteriormente!");
        }

        List<Aluno> todosAlunos = alunoRepository.findAll();

        todosAlunos.forEach(aluno -> {
            Double valorBase = (aluno.getTurma() != null
                    && aluno.getTurma().getValorMensalidade() != null)
                    ? aluno.getTurma().getValorMensalidade()
                    : 240.00;

            Pagamento novoPag = new Pagamento();
            novoPag.setAluno(aluno);
            novoPag.setMes(mesReferencia);
            novoPag.setValor(valorBase);
            novoPag.setPago(false);
            pagamentoRepository.save(novoPag);
        });

        return "Sucesso! Foram geradas " + todosAlunos.size()
                + " mensalidades para o mês de " + mesReferencia;
    }

    public List<DevedorDTO> listarDevedoresPorMesETurma(String mes, UUID turmaId) {
        return pagamentoRepository.findDevedoresByMesAndTurma(mes, turmaId)
                .stream()
                .map(p -> new DevedorDTO(
                        p.getAluno().getNome(),
                        p.getMes(),
                        p.getValor()
                ))
                .toList();
    }

    public ResumoCaixaDTO gerarResumoDoMes(String mes) {
        List<Pagamento> pagamentos = pagamentoRepository.findByMes(mes);

        Double recebido = pagamentos.stream()
                .filter(Pagamento::isPago)
                .mapToDouble(Pagamento::getValor)
                .sum();

        Double pendente = pagamentos.stream()
                .filter(p -> !p.isPago())
                .mapToDouble(Pagamento::getValor)
                .sum();

        return new ResumoCaixaDTO(recebido, pendente, pagamentos.size());
    }
}
