package com.badlogic.memoria.Telas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.memoria.MemoriaGame;

/** First screen of the application. Displayed after the application is created. */
public class WinScreen implements Screen {

    final MemoriaGame game;
    /* Imagens do cenário. */
    private Texture frutasTexture;
    /* Sons do jogo. */
    private Sound winSound;

    public WinScreen(final MemoriaGame game) {
        this.game = game;

        /* Inicialização das imagens. */
        frutasTexture = new Texture("Cenario/frutasFelizes.png");

        /* Pausa a música do jogo. */
        this.game.pauseMusic();

        /* Inicialização dos sons e música. */
        winSound = Gdx.audio.newSound(Gdx.files.internal("Sons_Musics/win.mp3"));
        winSound.play(1f); 
    }

    @Override
    public String toString() {
        return "WinScreen"; // Identificador da tela
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
        /* Faz alguma coisa quando a tela é tocada. */
        if (Gdx.input.isTouched()) {
            /* Passa para o próximo nível. */
            this.game.proximoNivel();

            if (this.game.getNivel() == 1) {
                /* Salva os dados da partida. */
                this.game.salvarDados();
                /* Muda para a próxima tela. */
                this.game.setScreen(new MenuScreen(this.game)); 
            }
            else {
                /* Muda para a próxima tela. */
                this.game.setScreen(new GameScreen(this.game)); 
            }

            /* Retorna a música do jogo. */
            this.game.playMusic();
			dispose();
		}
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

        // Termina o desenho
        this.game.spritebatch.end();


        this.game.fonte.draw("Você ganhou!!!", 250,500);
        this.game.fonte.draw("Click para ir para o nível " + this.game.whichProximoNivel(), 80, 110);
        this.game.fonte.draw("Tempo: " + this.game.getPartida().getTempo(this.game.getNivel() - 1), 60, 40);

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
        this.winSound.dispose();
    }
}