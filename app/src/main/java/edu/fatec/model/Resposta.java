package edu.fatec.model;

import java.util.Date;

/**
 * Entity Resposta responsável pelo gerenciamento de atributos de respostas.
 *
 * @author Bruno Henrique Calil, Gabriel Queiroz e Victor Hugo.
 *
 */
public class Resposta {
    private int idResposta;
    private String resposta;
    private String criador;
    private Date dataCriacao;
    private int rank;
    private boolean flagProfessor;
    private boolean flagCriador;

    public int getIdResposta() {
        return idResposta;
    }

    public void setIdResposta(int idResposta) {
        this.idResposta = idResposta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isFlagProfessor() {
        return flagProfessor;
    }

    public void setFlagProfessor(boolean flagProfessor) {
        this.flagProfessor = flagProfessor;
    }

    public boolean isFlagCriador() {
        return flagCriador;
    }

    public void setFlagCriador(boolean flagCriador) {
        this.flagCriador = flagCriador;
    }

    public String getCriador() {
        return criador;
    }

    public void setCriador(String criador) {
        this.criador = criador;
    }

    @Override
    public String toString() {
        return "Resposta [idResposta=" + idResposta + ", resposta=" + resposta + ", criador=" + criador
                + ", dataCriacao=" + dataCriacao + ", rank=" + rank + ", flagProfessor=" + flagProfessor
                + ", flagCriador=" + flagCriador + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((criador == null) ? 0 : criador.hashCode());
        result = prime * result + ((dataCriacao == null) ? 0 : dataCriacao.hashCode());
        result = prime * result + (flagCriador ? 1231 : 1237);
        result = prime * result + (flagProfessor ? 1231 : 1237);
        result = prime * result + idResposta;
        result = prime * result + rank;
        result = prime * result + ((resposta == null) ? 0 : resposta.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Resposta other = (Resposta) obj;
        if (criador == null) {
            if (other.criador != null)
                return false;
        } else if (!criador.equals(other.criador))
            return false;
        if (dataCriacao == null) {
            if (other.dataCriacao != null)
                return false;
        } else if (!dataCriacao.equals(other.dataCriacao))
            return false;
        if (flagCriador != other.flagCriador)
            return false;
        if (flagProfessor != other.flagProfessor)
            return false;
        if (idResposta != other.idResposta)
            return false;
        if (rank != other.rank)
            return false;
        if (resposta == null) {
            if (other.resposta != null)
                return false;
        } else if (!resposta.equals(other.resposta))
            return false;
        return true;
    }

}
