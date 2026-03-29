package com.eiba.System_Finances.service;

import com.eiba.System_Finances.DTO.DevedorDTO;
import com.eiba.System_Finances.DTO.ResumoCaixaDTO;
import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Pagamento;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PagamentoService {



    @Autowired
    private PagamentoRepository pagamentoRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;

    public Pagamento cadastrarPagamento(Pagamento pagamento) {
        // 1. Validação de Duplicidade
        boolean jaExiste = pagamentoRepository.existsByAlunoIdAndMes(
                pagamento.getAlunoId(),
                pagamento.getMes()
        );

        if (jaExiste) {
            throw new RuntimeException("Erro: Já existe um lançamento para o mês "
                    + pagamento.getMes() + " para este aluno.");
        }

        // 2. Se não existir, salva normalmente
        return pagamentoRepository.save(pagamento);
    }

    public List<Pagamento> listarPagamentos (){
        return pagamentoRepository.findAll();
    }

    public List<Pagamento> ListarPendentes(String alunoCPF){
        return pagamentoRepository.findByAlunoIdAndPago(alunoCPF, false);
    }

    public List<DevedorDTO> listarDevedoresComNome() {
        List<Pagamento> devedores = pagamentoRepository.findByPagoFalse();

        // Transformamos a lista de Pagamentos em uma lista de DevedorDTO
        return devedores.stream().map(pagamento -> {
            // Para cada pagamento, buscamos o aluno pelo ID
            String nome = alunoRepository.findById(pagamento.getAlunoId())
                    .map(aluno -> aluno.getNome())
                    .orElse("Aluno não identificado");

            return new DevedorDTO(nome, pagamento.getMes(), pagamento.getValor());
        }).toList();
    }

    public Pagamento darBaixaPagamento(String id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        pagamento.setPago(true);
        pagamento.setDataPagamento(LocalDateTime.now()); // Salva o dia e a hora exata da baixa

        return pagamentoRepository.save(pagamento);
    }

    public List<DevedorDTO> listarDevedoresPorMes(String mes) {
        List<Pagamento> devedores = pagamentoRepository.findByMesAndPagoFalse(mes);

        return devedores.stream().map(pagamento -> {
            String nome = alunoRepository.findById(pagamento.getAlunoId())
                    .map(aluno -> aluno.getNome())
                    .orElse("Aluno não encontrado");

            return new DevedorDTO(nome, pagamento.getMes(), pagamento.getValor());
        }).toList();
    }

    public String gerarRecibo(String pagamentoId) {
        Pagamento pag = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        Aluno aluno = alunoRepository.findById(pag.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

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

    public String gerarMensalidadesDoMes(String mesReferencia, Double valorPadrao) {
        // 1. Busca todos os alunos cadastrados
        List<Aluno> todosAlunos = alunoRepository.findAll();

        if (todosAlunos.isEmpty()) {
            throw new RuntimeException("Não há alunos cadastrados para gerar cobranças.");
        }

        // 2. Cria um pagamento para cada um deles
        todosAlunos.forEach(aluno -> {
            Pagamento novoPag = new Pagamento();
            novoPag.setAlunoId(aluno.getId());
            novoPag.setMes(mesReferencia);
            novoPag.setValor(valorPadrao);
            novoPag.setPago(false); // Começa como devedor

            pagamentoRepository.save(novoPag);
        });

        return "Sucesso! Foram geradas " + todosAlunos.size() + " mensalidades para o mês de " + mesReferencia;
    }

    public ResumoCaixaDTO gerarResumoDoMes(String mes) {
        // Agora o repository vai encontrar!
        List<Pagamento> pagamentos = pagamentoRepository.findByMes(mes);

        Double recebido = pagamentos.stream()
                .filter(p -> p.isPago()) // Use isPago()
                .mapToDouble(p -> p.getValor())
                .sum();

        Double pendente = pagamentos.stream()
                .filter(p -> !p.isPago()) // Use !isPago()
                .mapToDouble(p -> p.getValor())
                .sum();

        // Retorna o DTO que você criou
        return new ResumoCaixaDTO(recebido, pendente, pagamentos.size());
    }


}
