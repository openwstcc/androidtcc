package edu.fatec.model;

/**
 * Entity Materia responsável pelo gerenciamento de atributos de matérias.
 *
 * @author Bruno Henrique Calil, Gabriel Queiroz e Victor Hugo.
 *
 */
public class Materia {
    private int idMateria;
    private String materia;
    private int semestre;
    private Boolean marcado;

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    @Override
    public String toString() {
        return "Materia [idMateria=" + idMateria + ", materia=" + materia + ", semestre=" + semestre + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idMateria;
        result = prime * result + ((materia == null) ? 0 : materia.hashCode());
        result = prime * result + semestre;
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
        Materia other = (Materia) obj;
        if (idMateria != other.idMateria)
            return false;
        if (materia == null) {
            if (other.materia != null)
                return false;
        } else if (!materia.equals(other.materia))
            return false;
        if (semestre != other.semestre)
            return false;
        return true;
    }

    public Boolean getMarcado() {
        return marcado;
    }

    public void setMarcado(Boolean marcado) {
        this.marcado = marcado;
    }
}
