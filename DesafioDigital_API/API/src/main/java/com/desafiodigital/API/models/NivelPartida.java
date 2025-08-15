package com.desafiodigital.API.models;

import jakarta.persistence.*;

@Entity
@Table(name = "nivel_partida")
public class NivelPartida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nivel_partida")
    private int id;

    @Column(name = "tempo")
    private float tempo;

    @Column(name = "num_fase")
    private int fase;

    @Column(name = "num_nivel")
    private int nivel;

    @Column(name = "num_erros")
    private int qtdErros;

    @Column(name = "pontuacao")
    private int pontuacao;

    @ManyToOne
    @JoinColumn(name = "id_partida", nullable = false)
    private Partida partida;

    public NivelPartida() {
        this.fase = 1;
        this.nivel = 1;
        zerar();
    }

    public NivelPartida(int id, Partida partida) {
        this.id = id;
        this.partida = partida;
        this.fase = 1;
        this.nivel = 1;
        zerar();
    }

    public void zerar() {
        this.tempo = 0;
        this.qtdErros = 0;
        this.pontuacao = 0;
    }

    public void somaErro() { this.qtdErros++; }
    public void somaPontuacao(int valor) { this.pontuacao += valor; }
    public void subtraiPontuacao(int valor) { this.pontuacao -= valor; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public float getTempo() { return tempo; }
    public void setTempo(float tempo) { this.tempo = tempo; }
    public int getFase() { return fase; }
    public void setFase(int fase) { this.fase = fase; }
    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }
    public int getQtdErros() { return qtdErros; }
    public void setQtdErros(int qtdErros) { this.qtdErros = qtdErros; }
    public int getPontuacao() { return pontuacao; }
    public void setPontuacao(int pontuacao) { this.pontuacao = pontuacao; }
    public Partida getPartida() { return partida; }
    public void setPartida(Partida partida) { this.partida = partida; }
}
