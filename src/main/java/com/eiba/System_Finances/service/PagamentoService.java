package com.eiba.System_Finances.service;

import com.eiba.System_Finances.entity.Pagamento;
import com.eiba.System_Finances.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

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

    public List<Pagamento> listarDevedores() {
        return pagamentoRepository.findByPagoFalse();
    }


}
