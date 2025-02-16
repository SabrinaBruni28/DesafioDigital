package com.badlogic.desafiodigital.views;

import com.badlogic.desafiodigital.controllers.ControlaTexture;
import com.badlogic.desafiodigital.controllers.ControlaCartas;
import com.badlogic.desafiodigital.controllers.ControlaBotao;
import com.badlogic.desafiodigital.utils.Inicializadores;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.desafiodigital.DesafioDigital;
import com.badlogic.desafiodigital.utils.Draws;
import com.badlogic.desafiodigital.utils.Tempo;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import java.util.HashMap;

/** Classe abstrata para ser a base para outras tela de fase. */
public abstract class TelaFase implements Screen {
    final DesafioDigital game;

    // Define as proporções da janela, a "câmera" que será visualizada.
    protected FitViewport viewport;

    // Imagens da tela.
    protected ControlaTexture background, caixaMensagem;

    // Controladores dos botões.
    protected ControlaBotao controlaBotaoConfirmar;

    // Controladores de cartas.
    protected ControlaCartas cartas;

    // Define os estados da tela.
    protected enum Estado { INICIO, FINAL, ACERTO, ERRO, SELECAO, PROCESSANDO };

    // Define o render de cada estado.
    protected HashMap<Estado, Runnable> renders;

    // Define o estado atual e o padrão.
    protected Estado estadoAtual, estadoPadrao;

    public TelaFase(final DesafioDigital game) {
        this.game = game;
        inicializaEstados();

        // Inicia a contagem do tempo.
        Tempo.start();

        // Inicializa e define o tamanho do cenário.
		viewport = Inicializadores.inicializaViewport(this.game.getWorldWidth(), this.game.getWorldHeight());
        
        // Inicialização das imagens. 
        background = Inicializadores.inicializaControlaTexture(
            "Backgrounds/backgroundFases.png", 
            10f, 10f, this.game.getWorldWidth() - 20, this.game.getWorldHeight() - 10);

        // Inicialização dos botões.
        controlaBotaoConfirmar = Inicializadores.inicializaControlaBotao(
            "Botoes/botaoConfirmar.png", 1650.0f, 28.0f, 240.0f, 90.0f);
    }

    @Override
    public void dispose() {
        // Libera os assets.
        this.cartas.dispose();
        this.background.dispose();
        this.caixaMensagem.dispose();
        this.controlaBotaoConfirmar.dispose();
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
        //################################# INPUT #########################################//
        // Pega onde o toque aconteceu na tela.
        game.setMouse(); 
        // Converte as unidades da janela para as coordenadas do cenário.
        viewport.unproject(game.touchPos);
        
        //################################# DRAWS #########################################//
        // Limpa a tela.
        ScreenUtils.clear(game.getCorFundo());
        Draws.setupBatch(game.spritebatch, viewport);
            
        // Desenha o background.
        background.draw(game.spritebatch);

        // Desenha as cartas.
        cartas.draw(game.spritebatch, viewport, game.fonte, game.shapeRenderer);

        // Desenha botão
        game.getControlaBotaoMusica().draw(game.spritebatch);

        // Desenha qual a fase e nível..
        game.fonte.draw(
            "FASE " + game.jogo.getFaseAtual() + " - NÍVEL " + game.jogo.getNivelAtual(), 
            738, 1050, Color.WHITE, 1.2f, Color.BLACK, 1.0f);

        //#################################################################################//
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        game.fonte.resize( Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void pause() {
        Tempo.pause();
    }

    @Override
    public void resume() {
        if (estadoPadrao == Estado.INICIO) Tempo.start();
    }

    public void inicializaEstados() {
        // Inicializa o estado.
        estadoAtual = estadoPadrao = Estado.INICIO;
        renders = new HashMap<Estado, Runnable>();
        renders.put(Estado.ERRO, this :: renderErro);
        renders.put(Estado.FINAL, this :: renderFinal);
        renders.put(Estado.ACERTO, this :: renderAcerto);
        renders.put(Estado.INICIO, this :: renderPadrao);
        renders.put(Estado.SELECAO, this :: renderSelecao);
        renders.put(Estado.PROCESSANDO, this :: renderProcessando);
    }

    public void executaEstado() {
        // Recupera a função a ser executada de acordo com o estado atual.
        Runnable acao = renders.get(estadoAtual);
        // Executa a função se for um estado válido.
        if (acao != null) acao.run();
    }

    public abstract void renderErro();
    public abstract void renderAcerto();
    public abstract void renderSelecao();

    public void renderProcessando() {
        return;
    }

    public void renderPadrao() {
        // Atualiza a contagem do tempo.
        Tempo.render();
        //############################# MOVIMENTO #########################################//
        game.movementBotao(game.getControlaBotaoMusica());
        //#################################################################################//
    }

    public void renderFinal() {
        //################################# DRAWS #########################################//
        // Desenha o botão confirmar.
        controlaBotaoConfirmar.draw(game.spritebatch);
        
        //############################# MOVIMENTO #########################################//
        // Faz o movimento do botão confirmar se o mouse estiver em cima dele.
        game.movementBotao(controlaBotaoConfirmar);
        game.movementBotao(game.getControlaBotaoMusica());
        //#################################################################################//
    }

    @Override
    public String toString() {
        return "FaseAbstrataScreen"; // Identificador da tela.
    }

    public boolean touchedCarta() {
        return cartas.touchedCarta(game.getMouseX(),game.getMouseY(), game.getClickSound());
    }

    public void mudaBotao(ControlaBotao botao) {
        botao.mudaBotao(
            game.getMouseX(),game.getMouseY(), 1.2f);
    }

    public void setBolderColors(boolean bolderColor) {
        if (bolderColor) cartas.setBolderColors();
    }
}
