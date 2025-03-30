package com.badlogic.desafiodigital.dao;

import com.badlogic.desafiodigital.models.NivelPartida;
import com.badlogic.desafiodigital.models.Partida;
import com.badlogic.desafiodigital.models.Aluno;
import com.badlogic.desafiodigital.models.Turma;
import com.badlogic.desafiodigital.utils.Arquivos;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DAOController {
    // Atributos

    private Connection connection;
    
    // Referências à entidades
    private Aluno aluno;
    private Turma turma;
    private Partida partida;
    private NivelPartida nivelPartida;

    // Especialistas
    private DAOAluno daoAluno;
    private DAOTurma daoTurma;
    private DAOPartida daoPartida;
    private DAONivelPartida daoNivelPartida;

    // Construtores

    public DAOController() {
        daoAluno = new DAOAluno();
        daoTurma = new DAOTurma();
        daoPartida = new DAOPartida();
        daoNivelPartida = new DAONivelPartida();
    }

    public DAOController(Aluno aluno, Turma turma, Partida partida, NivelPartida nivelPartida) {
        // Assume referências à objetos externos, que espelharão valores a serem armazenados ou que foram armazenados.
        this.aluno = aluno;
        this.turma = turma;
        this.partida = partida;
        this.nivelPartida = nivelPartida;

        daoAluno = new DAOAluno();
        daoTurma = new DAOTurma();
        daoPartida = new DAOPartida();
        daoNivelPartida = new DAONivelPartida();
    }



    // Métodos públicos
    
    /**
     * Tenta realizar a conexão com o banco de dados. O ideal é chamá-lo assincronamente, pois pode demorar e criar um gargalo.
     * @return Retorna false em caso de falha e imprime a mensagem da exceção no terminal.
    */
    public boolean conecta() {
        try {
            System.out.println("Tentando conexão com o banco de dados...");
            connect();
            System.out.println("Conexão com o banco realizada com sucesso!");
            return true;
        }
        catch(SQLException e) {
            System.out.println("Exceção na interação com o banco: " + e.getMessage() );
            return false;
        }
        catch(Exception e) {
            System.out.println("Algo saiu mal: " + e.getMessage() );
            return false;
        }
    }

    /**
     * Tenta desfazer a conexão com o banco de dados. O ideal é chamá-lo assincornamente, pois pode demorar e criar um gargalo.
     * Cuidado! Mesmo se a tentativa falhar, a conexão pode não estar mais ativa!
     * @return Retorna false em caso de falha e imprime a mensagem da exceção no terminal.
     */
    public boolean desconecta() {
        try {
            System.out.println("Encerrando conexão com o banco de dados...");
            disconnect();
            System.out.println("Conexão com o banco finalizada!");
            return true;
        }
        catch(SQLException e) {
            System.out.println("Exceção na interação com o banco: " + e.getMessage() );
            return false;
        }
        catch(Exception e) {
            System.out.println("Algo saiu mal: " + e.getMessage() );
            return false;
        }
    }

    /**
     * Tenta registrar uma nova turma no banco de dados.
     * @return Retorna falso se algo deu errado. Se der certo, a turma que foi inserida terá recebido um novo id.
     */
    public boolean registraTurma() {
        try {
            daoTurma.insert(turma, null);
            return true;
        }
        catch(SQLException e) {
            System.out.println("Exceção na interação com o banco: " + e.getMessage() );
        }
        catch(Exception e) {
            System.out.println("Algo saiu mal: " + e.getMessage() );
        }
        return false;
    }
    
    /**
     * Tenta registrar um novo aluno no banco de dados.
     * Atenção! É preciso que a turma do aluno já tenha sido determinada e esteja com id.
     * @return Retorna falso se algo deu errado. Se der certo, o aluno que foi inserido terá recebido um novo id.
     */
    public boolean registraAluno() {
        try {
            daoAluno.insert(aluno, new int[]{turma.getId()});
            return true;
        }
        catch(SQLException e) {
            System.out.println("Exceção na interação com o banco: " + e.getMessage() );
        }
        catch(Exception e) {
            System.out.println("Algo saiu mal: " + e.getMessage() );
        }
        return false;
    }
    
    /**
     * Tenta registrar o início de uma nova partida.
     * Atenção! É preciso que o aluno jogando já tenha sido determinado e esteja com id.
     * @return Retorna falso se algo deu errado. Se der certo, a partida que foi inserida terá recebido um novo id.
     */
    public boolean registraPartida() {
        try {
            daoPartida.insert( partida, new int[]{aluno.getId()} );
            return true;
        }
        catch(SQLException e) {
            System.out.println("Exceção na interação com o banco: " + e.getMessage() );
        }
        catch(Exception e) {
            System.out.println("Algo saiu mal: " + e.getMessage() );
        }
        return false;
    }

    /**
     * Tenta registrar um nível que acaba de ser jogado.
     * Atenção! É preciso que uma partida já tenha sido iniciada apropriadamente.
     * @return Retorna falso se algo deu errado. Se der certo, o nível que foi registrado terá recebido um novo id.
     */
    public boolean registraNivelPartida() {
        try {
            daoNivelPartida.insert( nivelPartida, new int[]{partida.getId()} );
            return true;
        }
        catch(SQLException e) {
            System.out.println("Exceção na interação com o banco: " + e.getMessage() );
        }
        catch(Exception e) {
            System.out.println("Algo saiu mal: " + e.getMessage() );
        }
        return false;
    }



    // Getters
    
    public boolean isConectado() {
        return (connection == null) ? false : true ;
    }

    

    // Comandos para interação com o banco

    private void connect() throws SQLException, Exception {
        // Estabelece a conexão com o banco
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(   
            // Conectando com: Banco da Prova de Conceito
            Arquivos.readBancoDeDadosFromFile("gameConfig/banco.json", "url"),
            Arquivos.readBancoDeDadosFromFile("gameConfig/banco.json", "user"),
            Arquivos.readBancoDeDadosFromFile("gameConfig/banco.json", "password")
        );

        // Atualiza a conexão para as classes especializadas
        daoAluno.setConnection(connection);
        daoTurma.setConnection(connection);
        daoPartida.setConnection(connection);
        daoNivelPartida.setConnection(connection);
    }
    
    private void disconnect() throws SQLException, Exception {
        // Encerra os atributos dos especialistas
        daoAluno.close();
        daoTurma.close();
        daoPartida.close();
        daoNivelPartida.close();
        
        // Encerra a conexão
        if(connection != null) {
            connection.close();
        }
    }
}