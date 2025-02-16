package com.badlogic.desafiodigital.dao;

import com.badlogic.desafiodigital.models.Persistivel;
import com.badlogic.desafiodigital.models.Turma;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

public class DAOTurma extends DAO {

    // Métodos úteis para manipulação dos persistíveis

    protected Persistivel build() throws SQLException, Exception {
        // Constrói a turma na posição atual da query
        Turma currentTurma = new Turma( query.getInt("id_turma") );
        currentTurma.setTurma( query.getString("nome_turma") );
        
        return currentTurma;
    }

    protected void build(Turma turma) throws SQLException, Exception {
        // Recebe a turma na posição atual da query
        turma.setId( query.getInt("id_turma") );
        turma.setTurma( query.getString("nome_turma") );
    }

    protected ArrayList<Persistivel> obtemTudoDe() throws SQLException, Exception {
        ArrayList<Persistivel> todasAsTurmas = new ArrayList<Persistivel>();
        
        // Puxa todas as turmas do banco
        select();

        // Itera a query, construindo e armazenando todos as turmas no array
        while( query.next() )
            todasAsTurmas.add( build() );
        
        return todasAsTurmas;
    }


    // Métodos para interagir com o banco

    protected void select() throws SQLException, Exception {
        query = sql.executeQuery("SELECT * FROM turma;");
        System.out.println("Query feita sem problemas.");
    }

    protected void insert(Persistivel persistivel, int[] foreign_keys) throws SQLException, Exception {
        // Espera que o persistível seja uma turma
        Turma turmaInserida = (Turma) persistivel;

        // Prepara o comando SQL para verificar se a turma já existe
        String select = "SELECT id_turma FROM turma WHERE nome_turma = ?;";
        sql = connection.prepareStatement(select);
        sql.setString(1, turmaInserida.getTurma() );

        // Verifica se já existe a turma e, caso sim, retorna o id e pára o método
        query = sql.executeQuery();
        if( query.next() ) {
            turmaInserida.setId( query.getInt(1) );
            return;
        }
        
        // Prepara o comando SQL para inserir a turma
        String insert = "INSERT INTO turma (nome_turma) VALUES (?) ON DUPLICATE KEY UPDATE id_turma = LAST_INSERT_ID(id_turma);";
        sql = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        sql.setString(1, turmaInserida.getTurma() );
        
        // Insere a turma no banco de dados
        sql.executeUpdate();
        System.out.println("Turma registrada com sucesso!");

        // Obtém o id gerado na inserção e altera o parâmetro passado
        query = sql.getGeneratedKeys();
        query.next();
        turmaInserida.setId( query.getInt(1) );

        // for para inserir os alunos da turma - Não é prioridade
    }

    protected void update(Persistivel persistivel, int[] foreign_keys) throws SQLException, Exception {
        // Não é prioridade, mas vou fazer depois
    }

    protected void delete(Persistivel persistivel, int[] foreign_keys) throws SQLException, Exception {
        // Não é prioridade, mas vou fazer depois
    }
}
