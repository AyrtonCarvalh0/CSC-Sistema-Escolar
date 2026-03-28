package com.eiba.System_Finances.entity;


import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "matricula")
public class Matrícula {


    private String id;

    private String alunoId;

    private String turmaId;

    public Matrícula() {
    }

    public Matrícula(String id, String alunoId, String turmaId) {
        this.id = id;
        this.alunoId = alunoId;
        this.turmaId = turmaId;
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

    public String getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(String turmaId) {
        this.turmaId = turmaId;
    }
}