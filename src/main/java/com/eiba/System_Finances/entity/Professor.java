package com.eiba.System_Finances.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection= "professor")
public class Professor {

    @Id
    private String id;

    private String name;
    private String cpf;
    private String telefone;
    private String email;

    public Professor(){

    }

    public Professor(String id, String name, String cpf, String telefone, String email) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefoneS(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
