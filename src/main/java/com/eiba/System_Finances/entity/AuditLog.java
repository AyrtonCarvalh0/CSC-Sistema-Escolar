package com.eiba.System_Finances.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String usuario;

    @Column(nullable = false)
    private String acao;

    @Column(nullable = false)
    private String entidade;

    private String detalhe;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    private String ip;

    public AuditLog() {}

    public AuditLog(String usuario, String acao, String entidade, String detalhe, String ip) {
        this.usuario  = usuario;
        this.acao     = acao;
        this.entidade = entidade;
        this.detalhe  = detalhe;
        this.ip       = ip;
        this.dataHora = LocalDateTime.now();
    }

    public UUID getId()            { return id; }
    public void setId(UUID id)     { this.id = id; }

    public String getUsuario()              { return usuario; }
    public void setUsuario(String usuario)  { this.usuario = usuario; }

    public String getAcao()             { return acao; }
    public void setAcao(String acao)    { this.acao = acao; }

    public String getEntidade()                 { return entidade; }
    public void setEntidade(String entidade)    { this.entidade = entidade; }

    public String getDetalhe()              { return detalhe; }
    public void setDetalhe(String detalhe)  { this.detalhe = detalhe; }

    public LocalDateTime getDataHora()                  { return dataHora; }
    public void setDataHora(LocalDateTime dataHora)     { this.dataHora = dataHora; }

    public String getIp()           { return ip; }
    public void setIp(String ip)    { this.ip = ip; }
}
