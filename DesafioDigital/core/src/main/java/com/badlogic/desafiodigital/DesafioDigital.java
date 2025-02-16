package com.badlogic.desafiodigital;

import com.badlogic.desafiodigital.controllers.ControlaBotao;
import com.badlogic.desafiodigital.controllers.ControlaFonte;
import com.badlogic.desafiodigital.controllers.ControlaJogo;
import com.badlogic.desafiodigital.utils.Inicializadores;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.desafiodigital.models.NivelPartida;
import com.badlogic.desafiodigital.utils.TaskExecutor;
import com.badlogic.desafiodigital.dao.DAOController;
import com.badlogic.desafiodigital.views.TelaTitulo;
import com.badlogic.desafiodigital.models.Partida;
import com.badlogic.desafiodigital.utils.Arquivos;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.desafiodigital.models.Aluno;
import com.badlogic.desafiodigital.models.Turma;
import com.badlogic.desafiodigital.utils.Tempo;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Game;
import java.util.Collections;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import java.util.Random;

/** Classe main, responsável por iniciar e controlar todo o jogo. */
public class DesafioDigital extends Game {

    // Cenário para as imagens.
    public SpriteBatch spritebatch;

    // Para cálculo de coordenadas.
    public Vector2 touchPos;
    
    // Tamanhos do view.
    private float worldWidth;
    private float worldHeight;

    // Cor de fundo do jogo.
    private Color corFundo;
    // Desenhar cores na tela.
    public ShapeRenderer shapeRenderer;

    // Música e sons do jogo.
    private Music musicaJogo;
    private Sound clickSound;
    private Sound hoverSound;
    private Sound erroSound;
    private Sound acertoSound;
    private Sound vitoriaSound;

    // Controla o botão de música.
    public ControlaBotao controlaBotaoMusica;

    // Fonte para texto.
    public ControlaFonte fonte;

    // Controla o fluxo do jogo.
    public ControlaJogo jogo;
    
    // Salva as informações da partida a cada nível.
    private NivelPartida nivelPartida;
    private DAOController controlaDAO;

    // Informações do usuário.
    private Aluno aluno;
    private Turma turma;

    @Override
    public void create() {
        // Inicialização as informações do usuário
        aluno = new Aluno();
        turma = new Turma();
        nivelPartida = new NivelPartida();

        // Inicialização do controlador do Banco de Dados.
        controlaDAO = new DAOController(aluno, turma, new Partida(), nivelPartida);

        // Criação da threads necessárias em todo o jogo.
        defineThreads(); 
        TaskExecutor.executeTask("conecta");

        // Inicialização do spritebatch.
        spritebatch = new SpriteBatch();

        // Inicialização do vetor de coordenadas.
        touchPos = new Vector2();

        // Inicialização do controlador de jogo.
        jogo = new ControlaJogo(3);

        // Inicialização da cor de fundo.
        corFundo = new Color(0xFF6D60FF);
        
        // Define o tamanho do view padrão.
        worldHeight = 1080.0f;
        worldWidth = 1920.0f;
        
        // Inicialização dos sons.
        clickSound = Gdx.audio.newSound(Gdx.files.internal("Sons/clique.mp3"));
        hoverSound = Gdx.audio.newSound(Gdx.files.internal("Sons/bolha.mp3"));
        erroSound = Gdx.audio.newSound(Gdx.files.internal("Sons/erro.mp3"));
        acertoSound = Gdx.audio.newSound(Gdx.files.internal("Sons/acerto.mp3"));
        vitoriaSound = Gdx.audio.newSound(Gdx.files.internal("Sons/vitoria.mp3"));

        // Inicialização dos botões.
        controlaBotaoMusica = Inicializadores.inicializaControlaBotao(
            "Botoes/volumeOn.png", "Botoes/volumeOff.png", 
            1800.0f, 940.0f, 80.0f, 80.0f);

        // Define a Música a ser tocada.
        defineMusica("Sons/musicaInicial.mp3", false);

        // Define outra música quando a primeira música acabar de tocar.
        musicaJogo.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                defineMusica("Sons/musicaLoop.mp3", true);
            }
        });
        
        // Inicialização da fonte de texto.
        fonte = Inicializadores.inicializaControlaFonte(spritebatch, Arquivos.getArquivoFnt(), Arquivos.getArquivoPng(), worldWidth, worldHeight);
        
        shapeRenderer = new ShapeRenderer();

        // Colocando fullscreen.
        toggleFullscreen();
        
        // Mudando para a primeira tela.
        setScreen(new TelaTitulo(this));
    }

    @Override
    public void dispose() {
        TaskExecutor.executeTask("desconecta");
        TaskExecutor.cancelTasks();
        TaskExecutor.dispose();

        // Exclui os assets da tela atualmente aberta.
        if (getScreen() != null) getScreen().dispose();
        
        // Exclui os assets quando o programa é fechado.
        this.musicaJogo.dispose();
        
        this.clickSound.dispose();
        this.hoverSound.dispose();
        this.erroSound.dispose();
        this.acertoSound.dispose();
        
        this.fonte.dispose();
        this.spritebatch.dispose();
        this.shapeRenderer.dispose();
        this.controlaBotaoMusica.dispose();

        super.dispose();
        Gdx.app.exit(); // Encerra o aplicativo
    }

    @Override
    public void render() {
        try {
            super.render(); // Executa o render normal
        } catch (Exception e) {
            // Registra o erro
            System.err.println("Erro crítico durante a renderização: " + e.getMessage());
            e.printStackTrace();
            
            // Libera recursos e encerra a aplicação
            dispose(); // Libera os recursos do jogo
        }
        // Se o jogador apertar enter, alterna entre fullscreen e modo janela.
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) toggleFullscreen();
    }

    @Override
    public void pause() {
        if (getScreen() != null) getScreen().pause();
        pauseMusic();
    }

    @Override
    public void resume() {
        Screen screen = getScreen();
        if (screen != null) {
            screen.resume();
            if (!screen.toString().equals("PontuaçãoScreen"))
                playMusic();
        }
    }
    
    public void toggleFullscreen() {
        if (Gdx.graphics.isFullscreen()) {
            // Volta para o modo janela.
            Gdx.graphics.setWindowedMode(800, 480);
        } 
        else {
            // Muda para o modo fullscreen.
            DisplayMode displayMode = Gdx.graphics.getDisplayMode();
            Gdx.graphics.setFullscreenMode(displayMode);
        }
    }
    
    @Override
    public String toString() {
        return "PrincipalScreen"; // Identificador da tela.
    }
    
    public void calculaPontuacao() {
        // Calcula a pontuação.
        int erros = nivelPartida.getQtdErros();
        int pontuacao = jogo.obterNivelAtual().calcularPontuacao(
            erros, Tempo.getElapsedTime()
        );
            
        // Salva a pontuação.
        nivelPartida.setPontuacao(pontuacao);
    }

    public void resetPontuacao() {
        // Reseta a pontuação e tempo.
        nivelPartida.zerar();
        Tempo.reset();
    }

    public void defineMusica(String musica, boolean loop) {
        musicaJogo = Gdx.audio.newMusic(Gdx.files.internal(musica));
        musicaJogo.setVolume(0.4f);
        musicaJogo.setLooping(loop);
        musicaJogo.play();
    }

    public boolean pauseMusic() {
        if (musicaJogo.isPlaying()) {
            musicaJogo.pause();
            return true;
        }
        return false;
    }

    public boolean playMusic() {
        if (controlaBotaoMusica.isWhithBotao()) { 
            musicaJogo.play();
            return true;
        }
        return false;
    }

    public void touchedBotaoMusica() {
        // Verifica se o botão foi clicado. 
        if (touchedBotao(controlaBotaoMusica)) {
            controlaBotaoMusica.mudaBotao();
            pauseMusic();
            playMusic();
        }
    }

    public void movementBotao(ControlaBotao botao) {
        botao.movementBotao(getMouseX(), getMouseY(), 1.2f, getHoverSound());
    }

    public boolean touchedBotao(ControlaBotao botao) {
        return botao.touchedBotao(getMouseX(), getMouseY(), getClickSound());
    }

    public void fisherYatesShuffle(ArrayList<?> lista) {
        Random random = new Random();
        for (int i = lista.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1); // Escolhe um índice aleatório de 0 até i
            // Troca as cartas[i] com cartas[j]
            Collections.swap(lista, i, j);
        }
    }

    public void defineThreads() {
        Runnable conecta = () -> {
            // Realiza a conexão com o banco de dados.
            controlaDAO.conecta();
        };
        TaskExecutor.createTask("conecta", conecta);

        Runnable desconecta = () -> {
            // Finaliza a conexão com o banco de dados.
            controlaDAO.desconecta();
        };
        TaskExecutor.createTask("desconecta", desconecta);

        Runnable iniciaPartida = () -> {
            controlaDAO.registraTurma();
            controlaDAO.registraAluno();
            controlaDAO.registraPartida();
        };
        TaskExecutor.createTask("iniciaPartida", iniciaPartida);

        Runnable registraNivel = () -> {
            controlaDAO.registraNivelPartida();
        };
        TaskExecutor.createTask("registraNivel", registraNivel);
    }

    public float getWorldWidth() {
        return worldWidth;
    }

    public float getWorldHeight() {
        return worldHeight;
    }
    
    public float getMouseX() {
        return touchPos.x;
    }

    public float getMouseY() {
        return touchPos.y;
    }
    
    public Sound getClickSound() {
        return clickSound;
    }
    
    public Sound getHoverSound() {
        return hoverSound;
    }

    public Color getCorFundo() {
        return corFundo;
    }

    public Sound getErroSound() {
        return erroSound;
    }

    public Sound getAcertoSound() {
        return acertoSound;
    }

    public Sound getVitoriaSound() {
        return vitoriaSound;
    }

    public Music getMusicaJogo() {
        return musicaJogo;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Turma getTurma() {
        return turma;
    }

    public NivelPartida getNivelPartida() {
        return nivelPartida;
    }

    public ControlaBotao getControlaBotaoMusica() {
        return controlaBotaoMusica;
    }

    public void setMouse() {
        touchPos.set(Gdx.input.getX(), Gdx.input.getY()); 
    }

    public void setNome(String nome) {
        this.aluno.setNome(nome);
    }

    public void setTurma(String turma) {
        this.turma.setTurma(turma);
    }
}
