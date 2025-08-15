package com.desafiodigital.API.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "partida")
public class Partida{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partida")
    private int id;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_partida", nullable = false)
    private Date data;

    @ManyToOne
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;

    public Partida() {
        this.data = new Date();
    }

    public Partida(int id, Aluno aluno) {
        this.id = id;
        this.aluno = aluno;
        this.data = new Date();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
}
