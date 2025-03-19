package com.badlogic.memoria.DAO;

import java.sql.SQLException;
import java.util.ArrayList;

import com.badlogic.memoria.Partida;

public class DAOPartida extends DAO
{
    private Partida build() throws SQLException, Exception
    {
        Partida partida = new Partida(3);
        
        partida.setUsuario( query.getString("usuario") );
        float[] tempos = {
            query.getFloat("tempo_nivel1"),
            query.getFloat("tempo_nivel2"),
            query.getFloat("tempo_nivel3")
        };
        partida.setTempo(tempos);

        return partida;
    }
    
    public ArrayList<Partida> obterTodasPartidas() throws SQLException, Exception
    {
        select();
        ArrayList<Partida> partidas = new ArrayList<Partida>();
        while( query.next() )
            partidas.add( build() );
        return partidas;
    }
        
    private void select() throws SQLException, Exception
    {
        query = connection.prepareStatement("SELECT * FROM "+ tableName +";").executeQuery();
        System.out.println("Query feita sem problemas.");
    }
    
    public void insert(Partida partida) throws SQLException, Exception
    {
        String insert = "INSERT INTO "+ tableName +" VALUES (0, ?, ?, ?, ?);";
        sql = connection.prepareStatement(insert);
        sql.setString(1, partida.getUsuario() );
        sql.setFloat(2, partida.getTempo(0) );
        sql.setFloat(3, partida.getTempo(1) );
        sql.setFloat(4, partida.getTempo(2) );
        sql.executeUpdate();
        System.out.println("Partida registrada com sucesso!");
    }
    
    /*private void delete() throws SQLException, Exception
    {
        
    }
    
    private void update() throws SQLException, Exception
    {
        
    }*/
}
