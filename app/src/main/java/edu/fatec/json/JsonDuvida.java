package edu.fatec.json;

import java.util.Date;

/**
 * Entity Json de Duvida responsável pela integração entre o JSon e as
 * informações das dúvidas.
 *
 * @author Bruno Henrique Calil, Gabriel Queiroz e Victor Hugo.
 *
 */

public class JsonDuvida {
    private int idDuvida;
    private String titulo;
    private String conteudo;
    private Date dataCriacao;
    private int idUsuario;
    private int[] materias;
    private String[] tags;

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

    public int[] getMaterias() {
        return materias;
    }

    public void setMaterias(int[] materias) {
        this.materias = materias;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

}
