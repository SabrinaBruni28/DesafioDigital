package io.github.DesafioDigital.dao;

import io.github.DesafioDigital.models.Persistivel;
import io.github.DesafioDigital.models.Partida;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.Date;

public class DAOPartida extends DAO {
    
    // Métodos úteis para manipulação dos persistíveis

    protected Persistivel build() throws SQLException, Exception {
        // Constrói a partida na posição atual da query
        Partida currentPartida = new Partida( query.getInt("id_partida") );
        currentPartida.setData( query.getDate("data_partida") );
        
        return currentPartida;
    }
    
    protected ArrayList<Persistivel> obtemTudoDe() throws SQLException, Exception {
        ArrayList<Persistivel> todasAsPartidas = new ArrayList<Persistivel>();
        
        // Puxa todas as partidas do banco
        select();

        // Itera a query, construindo e armazenando todos as partidas no array
        while( query.next() )
            todasAsPartidas.add( build() );
        
        return todasAsPartidas;
    }
    

    // Métodos para interagir com o banco

    protected void select() throws SQLException, Exception {
        query = sql.executeQuery("SELECT * FROM partida;");
        System.out.println("Query feita sem problemas.");
    }

    protected void insert(Persistivel persistivel, int[] foreign_keys) throws SQLException, Exception {
        // Espera que o persistível seja uma partida
        Partida partidaInserida = (Partida) persistivel;

        // Prepara o comando SQL para inserir a partida
        String insert = "INSERT INTO partida (data_partida, id_aluno) VALUES (?, ?);";
        sql = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        sql.setDate(1, new Date( partidaInserida.getData().getTime() ) );
        sql.setInt(2, foreign_keys[0]);
        
        // Insere a partida no banco de dados
        sql.executeUpdate();
        System.out.println("Partida registrada com sucesso!");

        // Obtém o id gerado na inserção e altera o parâmetro passado
        query = sql.getGeneratedKeys();
        query.next();
        partidaInserida.setId( query.getInt(1) );
    }

    protected void update(Persistivel persistivel, int[] foreign_keys) throws SQLException, Exception {
        // Não é prioridade, mas vou fazer depois
    }

    protected void delete(Persistivel persistivel, int[] foreign_keys) throws SQLException, Exception {
        // Não é prioridade, mas vou fazer depois
    }

}
