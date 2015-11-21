package edu.fatec.model;

import java.util.Arrays;
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
    private String dataCriacao;
    private String criador;
    private int idUsuario;
    private int usuarioLogado;
    private int[] materias;
    private int qtdRespostas;
    private String[] tags;

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }


    public int getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(int usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }


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

    public String getCriador() {
        return criador;
    }

    public void setCriador(String criador) {
        this.criador = criador;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int[] getMaterias() {
        return materias;
    }

    public void setMaterias(int[] materias) {
        this.materias = materias;
    }

    public int getQtdRespostas() {
        return qtdRespostas;
    }

    public void setQtdRespostas(int qtdRespostas) {
        this.qtdRespostas = qtdRespostas;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Duvida [idDuvida=" + idDuvida + ", titulo=" + titulo + ", conteudo=" + conteudo + ", dataCriacao="
                + dataCriacao + ", criador=" + criador + ", idUsuario=" + idUsuario + ", materias="
                + Arrays.toString(materias) + ", qtdRespostas=" + qtdRespostas + ", tags=" + Arrays.toString(tags)
                + "]";
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


}
