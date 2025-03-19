package com.badlogic.memoria.Telas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.memoria.MemoriaGame;
import com.badlogic.memoria.Partida;
import com.badlogic.memoria.Botoes.Botao;
import com.badlogic.memoria.CaixaTexto.CaixaTexto;

/** First screen of the application. Displayed after the application is created. */
public class MenuScreen implements Screen {

    final MemoriaGame game;
    /* Imagens do cenário. */
    private Texture frutasTexture;
    /* Botão. */
    private Botao botao;
    /* Caixa de texto. */
    private CaixaTexto caixaTexto;

    public MenuScreen(final MemoriaGame game) {
        this.game = game;
        /* Inicializa o nível. */
        this.game.setNivel(1);
        /* Inicialização da partida. */
        this.game.setPartida(new Partida(this.game.getQntNivel()));
        /* Inicialização da caixa de texto. */
        caixaTexto = new CaixaTexto(150, 105, 510, 50);
        
        /* Inicialização das imagens. */
        frutasTexture = new Texture("Cenario/frutasFelizes.png");
        /* Inicialização do botão. */
        botao = new Botao("botao.jpg", "Sons_Musics/click.mp3", 9.0f, 1.0f, 2.5f, 1.5f);
    }

    @Override
    public String toString() {
        return "MenuScreen"; // Identificador da tela
    }

    @Override
    public void show() {
        // Prepare your screen here.
    }

    @Override
    public void render(float delta) {
        /* Desenha na tela. */
        draw();
        /* Detecta entradas do usuário. */
		input();
        /* Executa a lógica do programa. */
        logic();
    }

    private void input() {
         /* Pega onde o toque aconteceu na tela. */
         this.game.touchPos.set(Gdx.input.getX(), Gdx.input.getY()); 
         /* Converte as unidades da janela para as coordenadas do cenário. */
         this.game.viewport.unproject(this.game.touchPos);

         /* Faz alguma coisa quando clica ou toca a tela. */
        if (Gdx.input.isTouched()) { // If the user has clicked or tapped the screen
            /* Verifica se o botão foi clicado. */
            if (botao.touched(this.game.touchPos.x, this.game.touchPos.y)) {
                /* Salva o nome digitado na caixa de texto. */
                this.game.getPartida().setUsuario(caixaTexto.getNomeUsuario());
                /* Muda para a próxima tela. */
                this.game.setScreen(new GameScreen(this.game));
                dispose();
            }
        }
        /* Faz o movimento do botão se o mouse estiver em cima dele. */
        botao.movement(this.game.touchPos.x, this.game.touchPos.y);
    }

    private void logic() {
        /* */
    }

    private void draw() {

        /* Limpa a tela. */
        ScreenUtils.clear(Color.WHITE);
        this.game.viewport.apply();
    
        // Define a matriz de projeção do SpriteBatch
        this.game.spritebatch.setProjectionMatrix(this.game.viewport.getCamera().combined);
    
        // Inicia o desenho
        this.game.spritebatch.begin(); // Use o spriteBatch, não game.batch
            
            /* Desenha o background. */
            this.game.spritebatch.draw(this.game.backgroundTexture, 0, 0, this.game.getWorldWidth(), this.game.getWorldHeight());
            /* Desenha as frutas.. */
            this.game.spritebatch.draw(frutasTexture, 6.5f, 4, 7, 7);
            /* Desenha o botão.. */
            botao.draw(this.game.spritebatch);
            
        // Termina o desenho
        this.game.spritebatch.end();

        this.game.fonte.draw("Jogo da Memória", 215, 500);
        /* Desenha a caixa de texto. */
        caixaTexto.render();
    }

    @Override
    public void resize(int width, int height) {
        this.game.viewport.update(width, height, true); // true centers the camera
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        /* Exclui as variáveis quando a tela é fechada. */
        this.frutasTexture.dispose();
        this.botao.dispose();
        this.caixaTexto.dispose();
    }
}