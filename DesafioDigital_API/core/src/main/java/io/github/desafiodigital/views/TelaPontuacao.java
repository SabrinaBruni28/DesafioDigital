package io.github.desafiodigital.views;

import io.github.desafiodigital.controllers.ControlaTexture;
import io.github.desafiodigital.controllers.ControlaBotao;
import io.github.desafiodigital.utils.Inicializadores;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.desafiodigital.DesafioDigital;
import io.github.desafiodigital.models.Nivel;
import io.github.desafiodigital.utils.Draws;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;

public class TelaPontuacao implements Screen {
    final DesafioDigital game;

    // Define as proporções da janela, a "câmera" que será visualizada.
    public FitViewport viewport;

    // Imagens da tela.
    private ControlaTexture background, mascote, caixaPontuacao, caixaPontos;

    // Controladores dos botões.
    private ControlaBotao controlaBotao;

    private int pontuacao, pontuacaoMaxima;

    public TelaPontuacao(final DesafioDigital game) {
        this.game = game;

        // Pausa a música.
        if (game.pauseMusic()) game.getVitoriaSound().play();

        Nivel nivel = game.jogo.obterNivelAtual();
        pontuacao = game.getNivelPartida().getPontuacao();
        pontuacaoMaxima = nivel.getPontuacaoInicial() + nivel.getBonusTempo();

        // Inicializa e define o tamanho do cenário.
		viewport = Inicializadores.inicializaViewport(this.game.getWorldWidth(), this.game.getWorldHeight());

        // Inicialização das imagens. 
        background = Inicializadores.inicializaControlaTexture(
            "Backgrounds/backgroundInicial.png", 
            0f, 0f, this.game.getWorldWidth(), this.game.getWorldHeight());

        mascote = Inicializadores.inicializaControlaTexture(
            wichEstrela(pontuacao, pontuacaoMaxima), 
            this.game.getWorldWidth()/2 - 700.0f/2, 510f, 
            700.0f, 550.0f);

        caixaPontuacao = Inicializadores.inicializaControlaTexture(
            "Caixas/caixaTelaPontuacao.png", 
            this.game.getWorldWidth()/2 - 600.0f/2, 50.0f, 
            600.0f, 600.0f);
                
        caixaPontos = Inicializadores.inicializaControlaTexture(
            "Caixas/caixaPontos.png", 
            this.game.getWorldWidth()/2 - 300.0f/2, 110.0f, 
            300.0f, 150.0f);      
            
        // Inicialização do botão.
        controlaBotao = Inicializadores.inicializaControlaBotao(
            "Botoes/botaoContinuar.png", 
            1165.0f, 1f, 
            300.0f, 200.0f);
    }

    @Override
    public void dispose() {
        // Libera os assets.
        this.mascote.dispose();
        this.background.dispose();
        this.caixaPontos.dispose();
        this.controlaBotao.dispose();
        this.caixaPontuacao.dispose();
    }

    @Override
    public void show() {
        //
    }

    @Override
    public void hide() {
        //
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
            // Verifica se o botão confirmar foi clicado. 
            if (game.touchedBotao(controlaBotao)) {
                game.resetPontuacao();
                // Retorna a música se estiver ativada.
                game.playMusic();
                // Mudar para a próxima tela.
                game.setScreen(game.jogo.decideNextNivel(game));
                dispose(); 
            }
        }
        // Faz o movimento do botão se o mouse estiver em cima dele.
        game.movementBotao(controlaBotao);
    }

    private void draw() {
        // Limpa a tela.
        ScreenUtils.clear(Color.BLACK);
        Draws.setupBatch(game.spritebatch, viewport);

        // Desenha o background.
        background.draw(game.spritebatch);

        // Desenha mascote.
        mascote.draw(game.spritebatch);

        // Desenha as caixas.
        caixaPontuacao.draw(game.spritebatch);
        caixaPontos.draw(game.spritebatch);

        // Desenha o botão.
        controlaBotao.draw(game.spritebatch);
        game.getControlaBotaoMusica().draw(game.spritebatch);

        // Desenha qual a fase e nível..
        game.fonte.draw(
            "FASE " + game.jogo.getFaseAtual() + " - NÍVEL " + game.jogo.getNivelAtual(), 
            810, 600, Color.WHITE, 0.8f, Color.BLACK, 1.0f);

        // Desenha os textos.
        Draws.desenhaTextoProporcional(
            game.fonte, wichMensagem(pontuacao, pontuacaoMaxima), 
            Color.WHITE, 860f, 410f, 
            200f, 35f, 1.2f,
            Color.BLACK, 1.0f
        );

        Draws.desenhaTextoProporcional(
            game.fonte, "PONTUAÇÃO", 
            Color.WHITE, 860f, 300f, 
            200f, 50f, 1.3f, 
            Color.BLACK, 1.0f
        );

        Draws.desenhaTextoProporcional(
            game.fonte, String.valueOf(pontuacao), 
            Color.BLACK, 920f, 160f, 
            60f, 50f, 1.2f
        );
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        game.fonte.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void pause() {
        //
    }

    @Override
    public void resume() {
        //
    }

    public String wichEstrela(float pontuacaoConseguida, float pontuacaoMaxima) {
        float pontos = pontuacaoConseguida/pontuacaoMaxima;

        if (pontos < 0.60) return "Mascote/mascote1Estrela.png";
        else if (pontos >= 0.85) return "Mascote/mascote3Estrelas.png";
        else return "Mascote/mascote2Estrelas.png";
    }

    public String wichMensagem(float pontuacaoConseguida, float pontuacaoMaxima) {
        float pontos = pontuacaoConseguida/pontuacaoMaxima;

        if (pontos < 0.60) return "BOM!";
        else if (pontos >= 0.85) return "PERFEITO!";
        else return "MUITO BOM!";
    }

    @Override
    public String toString() {
        return "PontuaçãoScreen"; // Identificador da tela.
    }
}
