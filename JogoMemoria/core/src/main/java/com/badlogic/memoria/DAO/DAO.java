package com.badlogic.memoria.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DAO
{
    protected Connection connection;
    protected PreparedStatement sql;
    protected ResultSet query;
    protected String tableName;
    
    public void connect() throws SQLException, Exception
    {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection
        (
        /*url:*/      "jdbc:mysql://avnadmin:AVNS_TQzcWi6quRBV4XWor03@mysql-projeto-integrador-cdc-ufv-2024-projeto-integrador-2024.l.aivencloud.com:24451/defaultdb?ssl-mode=REQUIRED",
        ///*db*/        "defaultdb",
        /*user:*/     "avnadmin",
        /*password:*/ "AVNS_TQzcWi6quRBV4XWor03"
        );
        tableName = "partida";
    }
    
    public void disconnect() throws SQLException, Exception
    {
        if(query != null)
            query.close();
        
        if(sql != null)
            sql.close();
        
        if(connection != null)
            connection.close();
    }
}