package com.eiba.System_Finances.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "turma")
public class Turma {

    @Id
    private String id;
    private String nome;
    private Integer idadeMax;
    private Integer idadeMin;
    private Integer capacidade;

    private String professorId;

    public Turma(){

    }

    public Turma(String id, String nome, Integer idadeMax, Integer idadeMin, Integer capacidade, List<Matrícula> matrícula, Professor professor) {
        this.id = id;
        this.nome = nome;
        this.idadeMax = idadeMax;
        this.idadeMin = idadeMin;
        this.capacidade = capacidade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdadeMax() {
        return idadeMax;
    }

    public void setIdadeMax(Integer idadeMax) {
        this.idadeMax = idadeMax;
    }

    public Integer getIdadeMin() {
        return idadeMin;
    }

    public void setIdadeMin(Integer idadeMin) {
        this.idadeMin = idadeMin;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

}
