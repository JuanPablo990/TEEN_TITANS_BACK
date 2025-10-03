package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes;

public class MateriaInscrita {
    private String codigo;
    private String nombre;
    private int creditos;

    public MateriaInscrita(String codigo, String nombre, int creditos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
    }

    public MateriaInscrita(String materia1) {
    this.codigo = materia1;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getCreditos() { return creditos; }
}