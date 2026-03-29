package com.eiba.System_Finances.service;

import com.eiba.System_Finances.DTO.DevedorDTO;
import com.eiba.System_Finances.entity.Pagamento;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // 1. Busca o pagamento pelo ID único dele
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com o ID: " + id));

        // 2. Muda o status para pago
        pagamento.setPago(true);

        // 3. Salva a alteração
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


}
