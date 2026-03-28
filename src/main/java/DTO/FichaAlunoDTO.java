package DTO;

import com.eiba.System_Finances.entity.Pagamento;

import java.util.List;

public class FichaAlunoDTO {

    private String nomeAluno;
    private String turma;
    private String nomeResponsavel;
    private List<Pagamento> mensalidades;
    private Double valorTotalEmAberto;

    public FichaAlunoDTO(String nomeAluno, String turma, String nomeResponsavel, List<Pagamento> mensalidades) {
        this.nomeAluno = nomeAluno;
        this.turma = turma;
        this.nomeResponsavel = nomeResponsavel;
        this.mensalidades = mensalidades;
        this.valorTotalEmAberto = valorTotalEmAberto;
    }


    public Double getValorTotalEmAberto() {
        return valorTotalEmAberto;
    }

    public void setValorTotalEmAberto(Double valorTotalEmAberto) {
        this.valorTotalEmAberto = valorTotalEmAberto;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public List<Pagamento> getMensalidades() {
        return mensalidades;
    }

    public void setMensalidades(List<Pagamento> mensalidades) {
        this.mensalidades = mensalidades;
    }
}
