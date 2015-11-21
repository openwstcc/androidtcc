package edu.fatec.json;

import java.util.Arrays;

public class JsonPesquisa {
    private boolean titulo;
    private boolean conteudo;
    private boolean materia;
    private boolean tag;
    private String[] pesquisa;

    public boolean isTitulo() {
        return titulo;
    }

    public void setTitulo(boolean titulo) {
        this.titulo = titulo;
    }

    public boolean isConteudo() {
        return conteudo;
    }

    public void setConteudo(boolean conteudo) {
        this.conteudo = conteudo;
    }

    public boolean isMateria() {
        return materia;
    }

    public void setMateria(boolean materia) {
        this.materia = materia;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }

    public String[] getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(String[] pesquisa) {
        this.pesquisa = pesquisa;
    }

    @Override
    public String toString() {
        return "JsonPesquisa [titulo=" + titulo + ", conteudo=" + conteudo + ", materia=" + materia + ", tag=" + tag
                + ", pesquisa=" + Arrays.toString(pesquisa) + "]";
    }
}

