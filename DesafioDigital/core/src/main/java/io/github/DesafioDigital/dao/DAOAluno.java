package io.github.DesafioDigital.dao;

import io.github.DesafioDigital.models.Persistivel;
import io.github.DesafioDigital.models.Aluno;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

public class DAOAluno extends DAO {
    
    // Métodos úteis para manipulação dos persistíveis

    protected Persistivel build() throws SQLException, Exception {
        // Constrói o aluno na posição atual da query
        Aluno currentAluno = new Aluno( query.getInt("id_aluno") );
        currentAluno.setNome( query.getString("nome_aluno") );
        
        return currentAluno;
    }

    protected void build(Aluno aluno) throws SQLException, Exception {
        // Recebe o aluno na posição atual da query
        aluno.setId( query.getInt("id_aluno") );
        aluno.setNome( query.getString("nome_aluno") );
    }

    protected ArrayList<Persistivel> obtemTudoDe() throws SQLException, Exception {
        ArrayList<Persistivel> todosOsAlunos = new ArrayList<Persistivel>();
        
        // Puxa todos os alunos no banco
        select();

        // Itera a query, construindo e armazenando todos os alunos no array
        while( query.next() )
            todosOsAlunos.add( build() );
        
        return todosOsAlunos;
    }


    // Métodos para executar comandos no banco

    protected void select() throws SQLException, Exception {
        query = sql.executeQuery("SELECT * FROM aluno;");
        System.out.println("Query feita sem problemas.");
    }

    protected void insert(Persistivel persistivel, int[] foreign_keys) throws SQLException, Exception {
        // Espera que o persistível seja um aluno
        Aluno alunoInserido = (Aluno) persistivel;

        // Prepara o comando SQL para verificar se o aluno já existe
        String select = "SELECT id_aluno FROM aluno WHERE nome_aluno = ? AND id_turma = ?;";
        sql = connection.prepareStatement(select);
        sql.setString(1, alunoInserido.getNome() );
        sql.setInt(2, foreign_keys[0]);

        // Verifica se já existe o aluno e, caso sim, retorna o id e pára o método
        query = sql.executeQuery();
        if( query.next() ) {
            alunoInserido.setId( query.getInt(1) );
            return;
        }

        // Prepara o comando SQL para inserir o aluno
        String insert = "INSERT INTO aluno (nome_aluno, id_turma) VALUES (?, ?) ON DUPLICATE KEY UPDATE id_aluno = LAST_INSERT_ID(id_aluno);";
        sql = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        sql.setString(1, alunoInserido.getNome() );
        sql.setInt(2, foreign_keys[0]);
        
        // Insere o aluno no banco de dados
        sql.executeUpdate();
        System.out.println("Aluno registrado com sucesso!");

        // Obtém o id gerado na inserção e altera o parâmetro passado
        query = sql.getGeneratedKeys();
        query.next();
        alunoInserido.setId( query.getInt(1) );
    }

    protected void update(Persistivel persistivel, int[] foreign_keys) throws SQLException, Exception {
        // Não é prioridade, mas vou fazer depois
    }

    protected void delete(Persistivel persistivel, int[] foreign_keys) throws SQLException, Exception {
        // Não é prioridade, mas vou fazer depois
    }
}
