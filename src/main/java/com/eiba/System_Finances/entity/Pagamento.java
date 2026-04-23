package com.eiba.System_Finances.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    private String mes;
    private Double valor;
    private boolean pago;
    private LocalDateTime dataPagamento;

    public Pagamento() {}

    public Pagamento(UUID id, Aluno aluno, String mes, Double valor, boolean pago, LocalDateTime dataPagamento) {
        this.id = id;
        this.aluno = aluno;
        this.mes = mes;
        this.valor = valor;
        this.pago = pago;
        this.dataPagamento = dataPagamento;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }

    public String getMes() { return mes; }
    public void setMes(String mes) { this.mes = mes; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public boolean isPago() { return pago; }
    public void setPago(boolean pago) { this.pago = pago; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
}
