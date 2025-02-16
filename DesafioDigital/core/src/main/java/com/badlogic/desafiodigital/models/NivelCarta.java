package com.badlogic.desafiodigital.models;

import java.util.ArrayList;

public class NivelCarta extends Nivel {
    private ArrayList<Carta> cartas; 
    private ArrayList<String> dicas; 

    public NivelCarta(
            int pontuacaoInicial, 
            float tempoMinimo, float tempoMaximo, 
            int bonusTempo, int penalidadeTempo, 
            int erroMaximo, int penalidadeErro,
            ArrayList<Carta> cartas, ArrayList<String> dicas) {

        super(pontuacaoInicial, tempoMinimo, tempoMaximo, bonusTempo, 
            penalidadeTempo, erroMaximo, penalidadeErro);
        this.cartas = cartas;
        this.dicas = dicas;
    }

    public Carta obterCarta(int indice) {
        if (indice < 0 || indice >= cartas.size()) {
            System.out.println("Não disponível.");
            return null;
        }
        return cartas.get(indice); 
    }

    public String obterDica(int indice) {
        if (indice < 0 || indice >= dicas.size()) {
            System.out.println("Dica não encontrada."); 
            return null;
        }
        return dicas.get(indice); 
    }

    public ArrayList<Carta> getCartas() {
        return cartas; 
    }
    
    public ArrayList<String> getDicas() {
        return dicas; 
    }

    public void setCartas(ArrayList<Carta> cartas) {
        this.cartas = cartas; 
    }

    public void setDicas(ArrayList<String> dicas) {
        this.dicas = dicas; 
    }

    @Override
    public String toString() {
        return "NivelCarta [\ncartas=" + cartas.toString() + ", \ndicas=" + dicas.toString() + 
                ", \ngetPontuacaoInicial()=" + getPontuacaoInicial() + ", \ngetTempoMinimo()="
                + getTempoMinimo() + ", \ngetTempoMaximo()=" + getTempoMaximo() + ", \ngetBonusTempo()=" + getBonusTempo()
                + ", \ngetPenalidadeTempo()=" + getPenalidadeTempo() + ", \ngetErroMaximo()=" + getErroMaximo()
                + ", \ngetPenalidadeErro()=" + getPenalidadeErro() + "]";
    }
}