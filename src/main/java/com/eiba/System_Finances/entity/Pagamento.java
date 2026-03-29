package com.eiba.System_Finances.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "pagamento")
public class Pagamento {

    private String id;
    private String alunoId;
    private String mes;
    private Double valor;
    private boolean pago;
    private LocalDateTime dataPagamento; // Importe do java.time.LocalDateTime

    public Pagamento(String id, String alunoId, String mes, Double valor, boolean pago, LocalDateTime dataPagamento) {
        this.id = id;
        this.alunoId = alunoId;
        this.mes = mes;
        this.valor = valor;
        this.pago = pago;
        this.dataPagamento = dataPagamento;
    }

    public Pagamento() {

    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(String alunoId) {
        this.alunoId = alunoId;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }
}
