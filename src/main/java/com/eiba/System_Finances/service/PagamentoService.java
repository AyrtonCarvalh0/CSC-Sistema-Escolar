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

    public Pagamento MesPagamento (Pagamento pagamento){
        return pagamentoRepository.save((pagamento));
    }

    public List<Pagamento> listarPagamentos (){
        return pagamentoRepository.findAll();
    }

    public List<Pagamento> ListarPendentes(String alunoCPF){
        return pagamentoRepository.findByAlunoIdAndPago(alunoCPF, false);
    }


}
