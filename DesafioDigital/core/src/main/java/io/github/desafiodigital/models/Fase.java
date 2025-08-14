package io.github.desafiodigital.models;

import java.util.ArrayList;

public class Fase {
    private ArrayList<Nivel> niveis; 
    private String explicacao; 

    public Fase(String explicacao) {
        this.niveis = new ArrayList<>(); 
        this.explicacao = explicacao; 
    }

    public Nivel obterNivel(int indice) {
        if (indice < 0 || indice >= niveis.size()) {
            System.out.println("Esse nível não está disponível."); 
            return null;
        }
        return niveis.get(indice); 
    }

    public ArrayList<Nivel> getNiveis() {
        return niveis;
    }

    public void setNiveis(ArrayList<Nivel> niveis) {
        this.niveis = niveis; 
    }

    public String getExplicacao() {
        return explicacao;
    }

    public void setExplicacao(String explicacao) {
        this.explicacao = explicacao; 
    }

    public void adicionarNivel(Nivel nivel) {
        niveis.add(nivel); 
    }
}