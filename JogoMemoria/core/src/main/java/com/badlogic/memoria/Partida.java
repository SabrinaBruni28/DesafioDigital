package com.badlogic.memoria;

import java.util.ArrayList;

public class Partida {
    
    private String usuario;

    private ArrayList<Float> tempos;

    public Partida(int N) {
        tempos = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            tempos.add(0.0f); // Adiciona um novo objeto Float com valor 0.0
        }
    }

    public void setTempo(int index, float tempo) {
        // Verifica se o índice está dentro dos limites do ArrayList
        if (index >= 0 && index < this.tempos.size()) {
            this.tempos.set(index, tempo); // Atualiza o tempo na posição especificada
        } else {
            throw new IndexOutOfBoundsException("Índice fora dos limites do ArrayList");
        }
    }

    public Float getTempo(int index) {
        // Verifica se o índice está dentro dos limites do ArrayList
        if (index >= 0 && index < this.tempos.size()) {
            return this.tempos.get(index); // Retorna o tempo na posição especificada
        } else {
            throw new IndexOutOfBoundsException("Índice fora dos limites do ArrayList");
        }
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public ArrayList<Float> getTempo() {
        return tempos;
    }

    public void setTempo(float[] tempos) {
        this.tempos = new ArrayList<Float>();

        for(float temp : tempos){
            this.tempos.add(temp);
        }
    }
}
