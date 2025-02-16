package com.badlogic.desafiodigital.views;

import com.badlogic.desafiodigital.controllers.ControlaCenario;
import com.badlogic.desafiodigital.controllers.ControlaBotao;
import com.badlogic.desafiodigital.controllers.ControlaCarta;
import com.badlogic.desafiodigital.models.ConfiguracaoFase;
import com.badlogic.desafiodigital.utils.Inicializadores;
import com.badlogic.desafiodigital.utils.TaskExecutor;
import com.badlogic.desafiodigital.models.NivelContexto;
import com.badlogic.desafiodigital.DesafioDigital;
import com.badlogic.desafiodigital.utils.Tempo;
import com.badlogic.desafiodigital.utils.Draws;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;

/** Tela do jogo do tipo contexto. */
public class TelaFaseContexto extends TelaFase {

    // Controladores dos botões.
    private ControlaBotao controlaBotaoX1, controlaBotaoX2;

    // Controlador de carta.
    private ControlaCarta cartaClicada;

    // Controlador de cenário.
    private ControlaCenario cenario;

    private NivelContexto nivel;

    public TelaFaseContexto(final DesafioDigital game) {
        super(game);

        // Inicialização das cartas.
        nivel = game.jogo.obterNivelContexto();
        ConfiguracaoFase config = game.jogo.getConfigFase();

        // Embaralha as opções.
        game.fisherYatesShuffle(nivel.getOpcoes());

        cartas = Inicializadores.inicializaControlaCartas(
            nivel.getOpcoes(),
            "Cartas/carta.png", "Cartas/carta.png", 
            config.getRow(), config.getCol(), 
            280f, 320f,
            config.getMarginX(), config.getMarginY(),
            config.getSpacingX(), config.getSpacingY()
        );
        
        // Inicializa as cartas viradas.
        cartas.setCartasFlipped(true);

        // Inicialização da caixa de mensagem.
        caixaMensagem = Inicializadores.inicializaControlaTexture(
            "Caixas/caixaMensagem.png", 
            this.game.getWorldWidth()/2 - 905.0f/2, 42.0f, 905.0f, 265.0f
        );
    
        // Inicialização dos botões.
        controlaBotaoX1 = Inicializadores.inicializaControlaBotao(
            "Botoes/botaoX.png", 1420.0f, 150.0f, 100.0f, 100.0f);
        
        controlaBotaoX2 = Inicializadores.inicializaControlaBotao(
            "Botoes/botaoX.png", 1320.0f, 220.0f, 100.0f, 100.0f);
        
        // Inicialização do cenário.
        cenario = Inicializadores.inicializaControlaCenario(
            nivel.getCenario(),
            100.0f, 100.f, 950.0f, 850.0f, 
            "Cartas/cartaCenario.png");
    }

    @Override
    public void dispose() {
        // Libera os assets.
        super.dispose();
        this.cenario.dispose();
        this.cartaClicada.dispose();
        this.controlaBotaoX1.dispose();
        this.controlaBotaoX2.dispose();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        //################################# DRAWS #########################################//
        // Desenha o cenário.
        cenario.draw(game.spritebatch, viewport, game.fonte);
        //#################################################################################//
        executaEstado();
    }

    @Override
    public String toString() {
        return "FaseContextoScreen"; // Identificador da tela.
    }

    @Override
    public void renderPadrao() {
        super.renderPadrao();
        //################################# INPUT #########################################//
        if (Gdx.input.justTouched()) {
            game.touchedBotaoMusica(); 
            // Verifica se alguma carta foi clicada.
            if (touchedCarta()) {
                // Pega a carta clicada.
                cartaClicada =  cartas.getClickedCard();
                if (cartaClicada != null) {
                    NivelContexto nivel = game.jogo.obterNivelContexto();
                    // Retorna se é a resposta correta.
                    boolean correta = cartas.cartaClicked(nivel.getIdResposta(), game.getAcertoSound(), game.getErroSound());
                    // Se a resposta estiver correta.
                    if (correta) {
                        estadoAtual = Estado.PROCESSANDO;
                        Tempo.executarComDelay(0.5f, () -> {
                            estadoAtual = Estado.ACERTO;
                        });
                    }
                    // Se a resposta estiver errada.
                    else {
                        game.getNivelPartida().somaErro();
                        estadoAtual = Estado.PROCESSANDO;
                        Tempo.executarComDelay(0.5f, () -> {
                            estadoAtual = Estado.ERRO;
                        });
                    }
                }
            }
        }
        //#################################################################################//
    }

    @Override
    public void renderErro() {
        //################################# INPUT #########################################//
        if (Gdx.input.justTouched()) { 
            if (game.touchedBotao(controlaBotaoX2)) {
                estadoAtual = estadoPadrao;
                cartaClicada.movementCard();
            }
        }

        //################################# DRAWS #########################################//
        // Desenha uma caixa de mensagem de resposta errada.
        Draws.drawMensagem(
            game.spritebatch, viewport, game.fonte,
            caixaMensagem, controlaBotaoX2, 
            "Resposta errada", Color.BLACK, 
            650, 200, 1.8f, 1.8f);
        
        //############################# MOVIMENTO #########################################//
        // Faz o movimento do botão x se o mouse estiver em cima dele.
        game.movementBotao(controlaBotaoX2);
        //#################################################################################//
    }

    @Override
    public void renderAcerto() {
        //################################# INPUT #########################################//
        if (Gdx.input.justTouched()) { 
            if (game.touchedBotao(controlaBotaoX1)) {
                // Pausa a cotagem de tempo.
                Tempo.pause();
                // Salva o tempo gasto.
                game.getNivelPartida().setTempo(Tempo.getElapsedTime());
                // Calcula a pontuação final.
                game.calculaPontuacao();
                // Manda para o banco de dados.
                TaskExecutor.executeTask("registraNivel");

                estadoPadrao = Estado.FINAL;
                estadoAtual = estadoPadrao;
                cartaClicada.movementCard();
            }
        }

        //################################# DRAWS #########################################//
        // Desenha uma carta de explicação sobre a alternativa.
        Draws.drawCartaExplicacao(
            game.spritebatch, viewport, game.fonte,
            cartaClicada, controlaBotaoX1,
            1080.0f, 80.0f, 2.5f);

        //############################# MOVIMENTO #########################################//
        // Faz o movimento do botão x se o mouse estiver em cima dele.
        game.movementBotao(controlaBotaoX1);
        //#################################################################################//
    }

    @Override
    public void renderSelecao() {
        //################################# INPUT #########################################//
        if (Gdx.input.justTouched()) { 
            if (game.touchedBotao(controlaBotaoX1)) {
                estadoAtual = estadoPadrao;
                cartaClicada.movementCard();
            }
        }

        //################################# DRAWS #########################################//
        // Desenha uma carta de explicação sobre a alternativa.
        Draws.drawCartaExplicacao(
            game.spritebatch, viewport, game.fonte,
            cartaClicada, controlaBotaoX1,
            1080.0f, 80.0f, 2.5f);

        //############################# MOVIMENTO #########################################//
        // Faz o movimento do botão x se o mouse estiver em cima dele.
        game.movementBotao(controlaBotaoX1);
        //#################################################################################//
    }

    @Override
    public void renderFinal() {
        super.renderFinal();
        //################################# INPUT #########################################//
        if (Gdx.input.justTouched()) { 
            game.touchedBotaoMusica();
            // Verifica se o botão confirmar foi clicado.
            if (game.touchedBotao(controlaBotaoConfirmar)) {
                // Mudar para a próxima tela.
                game.setScreen(new TelaPontuacao(game));
                dispose(); 
            }

            // Verifica se alguma carta foi clicada.
            else if (touchedCarta()) {
                cartaClicada =  cartas.getClickedCard();
                estadoAtual = Estado.PROCESSANDO;
                Tempo.executarComDelay(0.3f, () -> {
                    estadoAtual = Estado.SELECAO;
                });
            }
        }
        //#################################################################################//
    }
}
