package io.github.DesafioDigital.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

import io.github.DesafioDigital.models.Persistivel;
import io.github.DesafioDigital.models.NivelPartida;

public class DAONivelPartida extends DAO {

    // Métodos úteis para manipulação dos persistíveis

    protected Persistivel build() throws SQLException, Exception {
        // Constrói o nível atual da partida na posição atual da query
        NivelPartida currentNivelPartida = new NivelPartida( query.getInt("id_nivel_partida") );
        currentNivelPartida.setFase( query.getInt("fase_nivel_partida") );
        currentNivelPartida.setNivel( query.getInt("nivel_nivel_partida") );
        currentNivelPartida.setTempo( query.getInt("tempo_nivel_partida") );
        currentNivelPartida.setQtdErros( query.getInt("erros_nivel_partida") );
        currentNivelPartida.setPontuacao( query.getInt("pontuacao_nivel_partida") );
        
        return currentNivelPartida;
    }

    protected void build(NivelPartida nivelPartida) throws SQLException, Exception {
        // Recebe o nível atual da partida na posição atual da query
        nivelPartida.setId( query.getInt("id_nivel_partida") );
        nivelPartida.setFase( query.getInt("fase_nivel_partida") );
        nivelPartida.setNivel( query.getInt("nivel_nivel_partida") );
        nivelPartida.setPontuacao( query.getInt("pontuacao_nivel_partida") );
        nivelPartida.setQtdErros( query.getInt("erros_nivel_partida") );
        nivelPartida.setTempo( query.getFloat("tempo_nivel_partida") );
    }
    
    protected ArrayList<Persistivel> obtemTudoDe() throws SQLException, Exception {
        ArrayList<Persistivel> todasOsNiveisPartida = new ArrayList<Persistivel>();
        
        // Puxa todos os níveis de todas as partidas do banco
        select();

        // Itera a query, construindo e armazenando todos os níveis no array
        while( query.next() )
        todasOsNiveisPartida.add( build() );
        
        return todasOsNiveisPartida;
    }
    

    // Métodos para interagir com o banco

    protected void select() throws SQLException, Exception {
        query = sql.executeQuery("SELECT * FROM nivel_partida;");
        System.out.println("Query feita sem problemas.");
    }

    protected void insert(Persistivel persistivel, int[] foreign_keys) throws SQLException, Exception {
        // Espera que o persistível seja um nívelPartida
        NivelPartida nivelPartidaInserido = (NivelPartida) persistivel;

        // Prepara o comando SQL para inserir o nível da partida
        String insert = "INSERT INTO nivel_partida (num_fase, num_nivel, pontuacao, num_erros, tempo, id_partida) VALUES (?, ?, ?, ?, ?, ?);";
        sql = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        sql.setInt(1, nivelPartidaInserido.getFase() );
        sql.setInt(2, nivelPartidaInserido.getNivel() );
        sql.setInt(3, nivelPartidaInserido.getPontuacao() );
        sql.setInt(4, nivelPartidaInserido.getQtdErros() );
        sql.setFloat(5, nivelPartidaInserido.getTempo() );
        sql.setInt(6, foreign_keys[0]);
        
        // Insere o nível da partida no banco de dados
        sql.executeUpdate();
        System.out.println("Nível registrado com sucesso!");

        // Obtém o id gerado na inserção e altera o parâmetro passado
        query = sql.getGeneratedKeys();
        query.next();
        nivelPartidaInserido.setId( query.getInt(1) );
    }

    protected void update(Persistivel persistivel, int[] foreign_keys) throws SQLException, Exception {
        // Não é prioridade, mas vou fazer depois
    }

    protected void delete(Persistivel persistivel, int[] foreign_keys) throws SQLException, Exception {
        // Não é prioridade, mas vou fazer depois
    }
}
