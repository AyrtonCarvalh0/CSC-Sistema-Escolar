package com.eiba.System_Finances.DTO;

public class DevedorDTO {

        private String nomeAluno;
        private String mes;
        private Double valor;

        public DevedorDTO(String nomeAluno, String mes, Double valor) {
            this.nomeAluno = nomeAluno;
            this.mes = mes;
            this.valor = valor;
        }

        // Getters e Setters (Essenciais para o JSON funcionar!)
        public String getNomeAluno() { return nomeAluno; }
        public String getMes() { return mes; }
        public Double getValor() { return valor; }
    }
