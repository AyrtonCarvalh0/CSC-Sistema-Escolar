package com.eiba.System_Finances.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "boletim")
public class Boletim {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    private String disciplina;
    private Double nota;
    private Integer bimestre;
    private Integer ano;

    @Column(name = "lancado_em")
    private LocalDateTime lancadoEm;

    public Boletim() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }

    public String getDisciplina() { return disciplina; }
    public void setDisciplina(String disciplina) { this.disciplina = disciplina; }

    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }

    public Integer getBimestre() { return bimestre; }
    public void setBimestre(Integer bimestre) { this.bimestre = bimestre; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public LocalDateTime getLancadoEm() { return lancadoEm; }
    public void setLancadoEm(LocalDateTime lancadoEm) { this.lancadoEm = lancadoEm; }
}
