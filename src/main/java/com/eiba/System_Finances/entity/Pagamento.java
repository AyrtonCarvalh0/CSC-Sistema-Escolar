package com.eiba.System_Finances.entity;

import com.eiba.System_Finances.entity.Aluno;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pagamento")
public class Pagamento {

    private String id;
    private String alunoId;
    private String mes;
    private boolean pago;

    public Pagamento(String id, String alunoId, String mes, boolean pago) {
        this.id = id;
        this.alunoId = alunoId;
        this.mes = mes;
        this.pago = pago;
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
