package io.github.desafiodigital.models;

public class Aluno implements Persistivel {
    // Atributos
    private int id;
    private String nome;

    // Construtores
    public Aluno() {
            
    }

    public Aluno(int id) {
        this.id = id;
        this.nome = "";
    }

    public Aluno(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Métodos Get e Set para cada atributo
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
