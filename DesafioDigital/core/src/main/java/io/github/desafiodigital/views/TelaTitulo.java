package io.github.desafiodigital.views;

import io.github.desafiodigital.controllers.ControlaTexture;
import io.github.desafiodigital.controllers.ControlaBotao;
import io.github.desafiodigital.utils.Inicializadores;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.desafiodigital.DesafioDigital;
import io.github.desafiodigital.utils.Draws;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;

/** Tela inicial do jogo. */
public class TelaTitulo implements Screen {

    final DesafioDigital game;

    // Define as proporções da janela, a "câmera" que será visualizada.
    public FitViewport viewport;

    // Controlador do botão.
    private ControlaBotao controlaBotao;

    // Imagens da tela.
    private ControlaTexture background, logo;

    public TelaTitulo(final DesafioDigital game) {
        this.game = game;

        // Inicializa e define o tamanho do cenário.
		viewport = Inicializadores.inicializaViewport(this.game.getWorldWidth(), this.game.getWorldHeight());
        
        // Inicialização da imagem do background e da logo. 
        background = Inicializadores.inicializaControlaTexture(
            "Backgrounds/backgroundInicial.png", 0, 0, game.getWorldWidth(), game.getWorldHeight());

        logo = Inicializadores.inicializaControlaTexture(
            "Logo/logo.png", 590.0f, 400.0f, 800.0f, 600.0f);
    
        // Inicialização do controlador de botão.
        controlaBotao = Inicializadores.inicializaControlaBotao(
            "Botoes/botaoComecar.png", 
            this.game.getWorldWidth()/2 - 500.0f/2, 80.0f, 500.0f, 150.0f);
    }

    @Override
    public void dispose() {
        // Libera os assets.
        this.background.dispose();
        this.logo.dispose();
        this.controlaBotao.dispose();
    }

    @Override
    public void show() {
        /* */
    }

    @Override
    public void hide() {
        /* */
    }

    @Override
    public void render(float delta) {
        input();
        draw();
    }

    private void input() {
        // Pega onde o toque aconteceu na tela.
        game.setMouse(); 
        // Converte as unidades da janela para as coordenadas do cenário.
        viewport.unproject(game.touchPos);

        // Faz alguma coisa quando clica ou toca a tela.
        if (Gdx.input.justTouched()) { 
            game.touchedBotaoMusica();
            // Verifica se o botão foi clicado. 
            if (game.touchedBotao(controlaBotao)) {
                // Muda para a próxima tela.
                game.setScreen(new TelaLogin(game));
                dispose();
            }
       }
       // Faz o movimento do botão se o mouse estiver em cima dele.
       game.movementBotao(controlaBotao);
       game.movementBotao(game.getControlaBotaoMusica());
    }

    private void draw() {
        // Limpa a tela.
        ScreenUtils.clear(Color.BLACK);
        Draws.setupBatch(game.spritebatch, viewport);

        // Desenha o background.
        background.draw(game.spritebatch);
        // Desenha a logo.
        logo.draw(game.spritebatch);
        // Desenha o botão.
        controlaBotao.draw(game.spritebatch);
        game.getControlaBotaoMusica().draw(game.spritebatch);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        /* */
    }

    @Override
    public void resume() {
        /* */
    }
    
    @Override
    public String toString() {
        return "TituloScreen"; // Identificador da tela.
    }
}
