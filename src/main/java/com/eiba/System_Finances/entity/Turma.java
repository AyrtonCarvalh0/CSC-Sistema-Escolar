package com.eiba.System_Finances.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.UUID;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "turma")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;
    private Integer idadeMax;
    private Integer idadeMin;
    private Integer capacidade;
    private Double valorMensalidade;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    public Turma() {}

    public Turma(UUID id, String nome, Integer idadeMax, Integer idadeMin, Integer capacidade, Professor professor) {
        this.id = id;
        this.nome = nome;
        this.idadeMax = idadeMax;
        this.idadeMin = idadeMin;
        this.capacidade = capacidade;
        this.professor = professor;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getIdadeMax() { return idadeMax; }
    public void setIdadeMax(Integer idadeMax) { this.idadeMax = idadeMax; }

    public Integer getIdadeMin() { return idadeMin; }
    public void setIdadeMin(Integer idadeMin) { this.idadeMin = idadeMin; }

    public Integer getCapacidade() { return capacidade; }
    public void setCapacidade(Integer capacidade) { this.capacidade = capacidade; }

    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }

    public Double getValorMensalidade() { return valorMensalidade; }
    public void setValorMensalidade(Double valorMensalidade) { this.valorMensalidade = valorMensalidade; }
}
