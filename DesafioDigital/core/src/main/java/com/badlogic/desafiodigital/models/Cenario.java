package com.badlogic.desafiodigital.models;

public class Cenario {
    private String pergunta;
    private String imagemCenario;

    // Construtor
    public Cenario(String pergunta, String imagemCenario) {
        this.pergunta = pergunta;
        this.imagemCenario = imagemCenario;
    }

    // Getters e Setters
    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getImagemCenario() {
        return imagemCenario;
    }

    public void setImagemCenario(String imagemCenario) {
        this.imagemCenario = imagemCenario;
    }
}
