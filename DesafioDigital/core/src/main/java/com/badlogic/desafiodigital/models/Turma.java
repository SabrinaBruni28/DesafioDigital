package com.badlogic.desafiodigital.models;

import java.util.ArrayList;

public class Turma implements Persistivel {
    // Atributos
    private int id;
    private String turma;
    private ArrayList<Aluno> alunos;

    // Construtores
    public Turma() {
        this.alunos = new ArrayList<Aluno>();
    }
    
    public Turma(int id) {
        this.id = id;
        this.turma = "";
		this.alunos = new ArrayList<Aluno>();
    }

    public Turma(String turma) {
        this.turma = turma;
		this.alunos = new ArrayList<Aluno>();
    }

    public Turma(int id, String turma) {
        this.id = id;
        this.turma = turma;
		this.alunos = new ArrayList<Aluno>();
    }

    // Método para adicionar um único aluno na turma
    public void adicionaAluno(Aluno aluno) {
        alunos.add(aluno);
    }

    // Métodos Get e Set para cada atributo
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public ArrayList<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(ArrayList<Aluno> alunos) {
        this.alunos = alunos;
    }
}
