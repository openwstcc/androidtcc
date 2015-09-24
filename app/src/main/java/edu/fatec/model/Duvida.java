package edu.fatec.model;

import java.util.Date;

/**
 * Entity Duvida responsável pelo gerenciamento de atributos de dúvidas.
 *
 * @author Bruno Henrique Calil, Gabriel Queiroz e Victor Hugo.
 *
 */
public class Duvida {
    private int idDuvida;
    private String titulo;
    private String conteudo;
    private String criador;
    private Date dataCriacao;

    public int getIdDuvida() {
        return idDuvida;
    }

    public void setIdDuvida(int idDuvida) {
        this.idDuvida = idDuvida;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((conteudo == null) ? 0 : conteudo.hashCode());
        result = prime * result + ((dataCriacao == null) ? 0 : dataCriacao.hashCode());
        result = prime * result + idDuvida;
        result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
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
        Duvida other = (Duvida) obj;
        if (conteudo == null) {
            if (other.conteudo != null)
                return false;
        } else if (!conteudo.equals(other.conteudo))
            return false;
        if (dataCriacao == null) {
            if (other.dataCriacao != null)
                return false;
        } else if (!dataCriacao.equals(other.dataCriacao))
            return false;
        if (idDuvida != other.idDuvida)
            return false;
        if (titulo == null) {
            if (other.titulo != null)
                return false;
        } else if (!titulo.equals(other.titulo))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Duvida [idDuvida=" + idDuvida + ", titulo=" + titulo + ", conteudo=" + conteudo
                + ", dataCriacao=" + dataCriacao + "]";
    }

    public String getCriador() {
        return criador;
    }

    public void setCriador(String criador) {
        this.criador = criador;
    }

}
