package edu.fatec.json;

import java.util.Arrays;

public class JsonPesquisa {
    private String[] parametros;
    private String[] conteudo;

    public String[] getParametros() {
        return parametros;
    }
    public void setParametros(String[] parametros) {
        this.parametros = parametros;
    }
    public String[] getConteudo() {
        return conteudo;
    }
    public void setConteudo(String[] conteudo) {
        this.conteudo = conteudo;
    }
    @Override
    public String toString() {
        return "JsonPesquisa [parametros=" + Arrays.toString(parametros) + ", conteudo=" + Arrays.toString(conteudo)
                + "]";
    }


}

