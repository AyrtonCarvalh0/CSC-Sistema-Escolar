package com.eiba.System_Finances.DTO;

public class ResumoCaixaDTO {

    private Double totalRecebido;
    private Double totalPendente;
    private Double totalEsperado;
    private Integer quantidadePagamentos;

    // Construtor, Getters e Setters
    public ResumoCaixaDTO(Double recebido, Double pendente, Integer qtd) {
        this.totalRecebido = recebido;
        this.totalPendente = pendente;
        this.totalEsperado = recebido + pendente;
        this.quantidadePagamentos = qtd;
    }

    public Double getTotalRecebido() {
        return totalRecebido;
    }

    public void setTotalRecebido(Double totalRecebido) {
        this.totalRecebido = totalRecebido;
    }

    public Double getTotalPendente() {
        return totalPendente;
    }

    public void setTotalPendente(Double totalPendente) {
        this.totalPendente = totalPendente;
    }

    public Double getTotalEsperado() {
        return totalEsperado;
    }

    public void setTotalEsperado(Double totalEsperado) {
        this.totalEsperado = totalEsperado;
    }

    public Integer getQuantidadePagamentos() {
        return quantidadePagamentos;
    }

    public void setQuantidadePagamentos(Integer quantidadePagamentos) {
        this.quantidadePagamentos = quantidadePagamentos;
    }
}
