package com.badlogic.desafiodigital.models;

public class Carta {
    private int id;
    private String nome;
    private String descricao;
    private String imagemDispositivo;

    // Construtor
    public Carta(String nome, int id, String imagemDispositivo, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.imagemDispositivo = imagemDispositivo;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagemDispositivo() {
        return imagemDispositivo;
    }

    public void setImagemDispositivo(String imagemDispositivo) {
        this.imagemDispositivo = imagemDispositivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "\nCarta [id=" + id + ", nome=" + nome + ", descricao=" + descricao + ", imagemDispositivo="
                + imagemDispositivo + "]\n";
    }
}
