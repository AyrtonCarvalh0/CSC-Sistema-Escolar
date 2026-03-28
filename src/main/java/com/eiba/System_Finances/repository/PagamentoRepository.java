package com.eiba.System_Finances.repository;

import com.eiba.System_Finances.entity.Pagamento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagamentoRepository extends MongoRepository<Pagamento, String> {

    List<Pagamento> findByAlunoId(String alunoId);
    List<Pagamento> findByAlunoIdAndPago(String alunoId, boolean pago);
    List<Pagamento> findByPagoFalse();
    boolean existsByAlunoIdAndMes(String alunoId, String mes);

}
