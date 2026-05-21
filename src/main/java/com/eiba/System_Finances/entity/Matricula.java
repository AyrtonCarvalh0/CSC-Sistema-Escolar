package com.eiba.System_Finances.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "matricula")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "turma_id")
    private Turma turma;

    public Matricula() {}

    public Matricula(UUID id, Aluno aluno, Turma turma) {
        this.id = id;
        this.aluno = aluno;
        this.turma = turma;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }

    public Turma getTurma() { return turma; }
    public void setTurma(Turma turma) { this.turma = turma; }
}
