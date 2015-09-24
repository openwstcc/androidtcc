package edu.fatec.model;

import java.util.Date;

/**
 * Entity Usuario responsável pelo gerenciamento de atributos de usuários
 *
 * @author Bruno Henrique, Gabriel Queiroz e Victor Hugo.
 */
public class Usuario {
    private int idUsuario;
    private String perfil;
    private String nome;
    private String sobrenome;
    private String senha;
    private String telefone;
    private Date dataNasc;
    private String email;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataNasc == null) ? 0 : dataNasc.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + idUsuario;
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((perfil == null) ? 0 : perfil.hashCode());
        result = prime * result + ((senha == null) ? 0 : senha.hashCode());
        result = prime * result + ((sobrenome == null) ? 0 : sobrenome.hashCode());
        result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
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
        Usuario other = (Usuario) obj;
        if (dataNasc == null) {
            if (other.dataNasc != null)
                return false;
        } else if (!dataNasc.equals(other.dataNasc))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (idUsuario != other.idUsuario)
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (perfil == null) {
            if (other.perfil != null)
                return false;
        } else if (!perfil.equals(other.perfil))
            return false;
        if (senha == null) {
            if (other.senha != null)
                return false;
        } else if (!senha.equals(other.senha))
            return false;
        if (sobrenome == null) {
            if (other.sobrenome != null)
                return false;
        } else if (!sobrenome.equals(other.sobrenome))
            return false;
        if (telefone == null) {
            if (other.telefone != null)
                return false;
        } else if (!telefone.equals(other.telefone))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Usuario [idUsuario=" + idUsuario + ", perfil=" + perfil + ", nome=" + nome + ", sobrenome=" + sobrenome
                + ", senha=" + senha + ", telefone=" + telefone + ", dataNasc=" + dataNasc + ", email=" + email + "]";
    }
}
