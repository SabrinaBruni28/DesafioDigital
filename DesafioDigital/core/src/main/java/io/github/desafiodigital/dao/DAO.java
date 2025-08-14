package io.github.desafiodigital.dao;

import io.github.desafiodigital.models.Persistivel;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;

// Classe abstrata com tudo o que um dos DAO filhos deveria ter
public abstract class DAO {
    
    // Atributos
    protected Connection connection;
    protected ResultSet query;
    protected PreparedStatement sql;

    // Métodos úteis para manipulação dos persistíveis
    protected abstract Persistivel build() throws SQLException, Exception;
    protected abstract ArrayList<Persistivel> obtemTudoDe() throws SQLException, Exception;

    // Métodos para interagir com o banco
    protected abstract void select() throws SQLException, Exception;
    protected abstract void insert(Persistivel persistivel, int[] foreign_keys) throws SQLException, Exception;
    protected abstract void update(Persistivel persistivel, int[] foreign_keys) throws SQLException, Exception;
    protected abstract void delete(Persistivel persistivel, int[] foreign_keys) throws SQLException, Exception;

    public void close() throws SQLException, Exception {
        // Fecha a query, se houver
        if(query != null)
            query.close();

        // Fecha o prepared statement, se houver
        if(sql != null)
            sql.close();
    }
    
    // Getters e Setters

    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ResultSet getQuery() {
        return query;
    }
    public void setQuery(ResultSet query) {
        this.query = query;
    }

    public PreparedStatement getSql() {
        return sql;
    }
    public void setSql(PreparedStatement sql) {
        this.sql = sql;
    }
}
