package com.badlogic.memoria;

import java.sql.SQLException;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.memoria.DAO.DAOPartida;
import com.badlogic.memoria.Telas.MenuScreen;

/* 
Adicionar:
    V* Botões
    * Gifs de fogos de artifício quando ganha
    * Gifs de chuva quando perde
    V* Tempo para perder
    V* Níveis diferentes
    V* Melhor fonte de escrita
    * Como uso o show() e o hide()
    * Imagem no .jar
    * Imagem no app aberto
    V* Caixa de texto
    * Como manter a caixa quando muda o tamanho da janela.
    
Em um novo programa:
    * Imagens na frente
    * Nome com imagem atrás
    * Descrição destacada
    * Cartas selecionadas destacadas
    * Caixa de seleção
*/
/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MemoriaGame extends Game {

    /* Cenário para as imagens. */
    public SpriteBatch spritebatch;
    
	/* Define as proporções da janela. */
    public FitViewport viewport;
    /* Para cálculo de coordenadas. */
    public Vector2 touchPos;

    /* Música do jogo. */
    private Music music;
	/* Imagens */
    public Texture backgroundTexture;

    /* Fonte para texto. */
    public Fonte fonte;

    /* Tamanhos do cenário. */
    private float worldWidth;
    private float worldHeight;

    /* Atributos da partida. */
    private int nivel;
    private int qntNivel;

    private Partida partida;
    private DAOPartida dao;

    @Override
    public void create() {
        /* Define os níveis. */
        this.nivel = 1;
        this.qntNivel = 3;

        dao = new DAOPartida();
        
        /* Inicializa os cenários. */
        spritebatch = new SpriteBatch();

        fonte = new Fonte(1.0f);
		
        /* Inicializa e define o tamanho do cenário. */
		viewport = new FitViewport(20, 15);
        /* Centraliza o cenário. */
		viewport.getCamera().position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0); 
        // Pega os valores do tamanho do cenário
        this.worldWidth = this.viewport.getWorldWidth();
        this.worldHeight = this.viewport.getWorldHeight();
        /* Inicialização do vetor de coordenadas. */
        touchPos = new Vector2();
		/* Inicialização das imagens */
        backgroundTexture = new Texture("Cenario/background.jpg");
        
        /* Inicializa a música do jogo. */
        music = Gdx.audio.newMusic(Gdx.files.internal("Sons_Musics/musicgame.mp3"));
        
        /* Para tocar a música em loop no inicio do jogo. */
        music.setLooping(true);
        /* Define o voluma da música. */
        music.setVolume(.5f);
        /* Coloca a música para tocar. */
        music.play();
        
        /* Mudando para a primeira tela. */
        setScreen(new MenuScreen(this));
    }
    
    public void render() {
        super.render(); // important!
	}
    
    @Override
    public String toString() {
        return "PrincipalScreen"; // Identificador da tela
    }

    public float getWorldWidth() {
        return worldWidth;
    }

    public float getWorldHeight() {
        return worldHeight;
    }

    public void salvarDados(){
        // Tente salvar os dados no banco
        try {
            // Faz a conexão com o banco. 
            dao.connect();
            // Insere os dados da partida no banco.
            dao.insert(partida);
            System.out.println("Banco de Dados Atualizado.");
            // Encerra a conexão com o banco. 
            dao.disconnect();
        }
        // Caso ocorra erro ao acessar o banco.
        catch(SQLException e){
            System.out.println("Erro ao conectar ao banco:");
            System.out.println(e.getMessage());
        } 
        // Caso ocorra algum outro erro. 
        catch(Exception e){
            System.out.println("Erro fatal:");
            System.out.println(e.getMessage());
        }
    }
    
    public void dispose() {
        /* Salva os dados no Banco. */
        if (partida.getUsuario() != null) salvarDados();

        /* Exclui os assets quando o programa é fechado. */
		this.spritebatch.dispose();
		this.backgroundTexture.dispose();
        this.music.dispose();
        this.fonte.dispose();

        super.dispose();
        
        /* Exclui os assets da tela atualmente aberta. */
        if (getScreen() != null) getScreen().dispose();
        
        /* Faz coleta de lixo. */
        System.gc();
	}

    public void playMusic(){
        music.play();
    }

    public void pauseMusic(){
        music.pause();
    }

    public int proximoNivel(){
        if (nivel == qntNivel) this.nivel = 1;
        else this.nivel += 1;
        return nivel;
    }

    public int whichProximoNivel(){
        if (nivel == qntNivel) return 1;
        else return nivel + 1;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel){
        this.nivel = nivel;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public int getQntNivel() {
        return qntNivel;
    }
}