package io.github.DesafioDigital.models;

import java.util.Date;

public class Partida implements Persistivel {
    // Atributos
    private int id;
    private Date data;

    // Construtores
    public Partida() {
		this.data = new Date();
    }

    public Partida(int id) {
        this.id = id;
		this.data = new Date();
    }

    // Métodos Get e Set para cada atributo
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
