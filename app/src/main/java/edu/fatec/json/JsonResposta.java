package edu.fatec.json;

public class JsonResposta {
    private String criador;
    private String resposta;
    private String dataCriacao;
    private boolean flagCriador;
    private boolean flagProfessor;
    private int idResposta;
    private int rank;
    private int idUsuario;
    private int idDuvida;
    private int usuarioLogado;
    private boolean deuLike;


    public String getCriador() {
        return criador;
    }

    public void setCriador(String criador) {
        this.criador = criador;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean isFlagCriador() {
        return flagCriador;
    }

    public void setFlagCriador(boolean flagCriador) {
        this.flagCriador = flagCriador;
    }

    public boolean isFlagProfessor() {
        return flagProfessor;
    }

    public void setFlagProfessor(boolean flagProfessor) {
        this.flagProfessor = flagProfessor;
    }

    public int getIdResposta() {
        return idResposta;
    }

    public void setIdResposta(int idResposta) {
        this.idResposta = idResposta;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdDuvida() {
        return idDuvida;
    }

    public void setIdDuvida(int idDuvida) {
        this.idDuvida = idDuvida;
    }

    public int getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(int usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public boolean isDeuLike() {
        return deuLike;
    }

    public void setDeuLike(boolean deuLike) {
        this.deuLike = deuLike;
    }
}
