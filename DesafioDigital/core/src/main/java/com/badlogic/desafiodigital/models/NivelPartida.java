package com.badlogic.desafiodigital.models;

public class NivelPartida implements Persistivel {
    // Atributos
    private int id;
    private int fase;
    private int nivel;
    private float tempo;
    private int qtdErros;
    private int pontuacao;
	
    // Construtores
    public NivelPartida() {
        this.fase = 1;
		this.nivel = 1;
        zerar(); // Inicializa pontuação, tempo e qtdErros com zero
    }

    public NivelPartida(int id) {
        this.id = id;
        this.fase = 1;
		this.nivel = 1;
        zerar(); // Inicializa pontuação, tempo e qtdErros com zero
    }
	

    // Método para zerar pontuação, tempo, e erros, além do id, já que será um novo nivelPartida
    public void zerar() {
        this.id = 0;
        this.tempo = 0;
        this.qtdErros = 0;
        this.pontuacao = 0;
    }
	
	// Método para incrementar o número de erros
    public void somaErro() {
        this.qtdErros++;
    }

    // Método para somar um valor à pontuação atual
    public void somaPontuacao(int valor) {
        this.pontuacao += valor;
    }

    // Método para subtrair um valor da pontuação atual
    public void subtraiPontuacao(int valor) {
        this.pontuacao -= valor;
    }

    // Métodos Get e Set para cada atributo
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getFase() {
        return fase;
    }

    public void setFase(int fase) {
        this.fase = fase;
    }
    
    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public float getTempo() {
        return tempo;
    }

    public void setTempo(float tempo) {
        this.tempo = tempo;
    }

    public int getQtdErros() {
        return qtdErros;
    }

    public void setQtdErros(int qtdErros) {
        this.qtdErros = qtdErros;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }
}
