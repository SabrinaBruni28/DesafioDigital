package io.github.desafiodigital.models;

// Interface para atribuir a objetos que podem ser gravados ou lidos do banco de dados
public interface Persistivel {
    // Marcadores indicando a posse do atributo ID
    public int getId();
    public void setId(int id);
}
