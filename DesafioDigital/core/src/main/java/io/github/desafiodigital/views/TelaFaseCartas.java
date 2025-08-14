package io.github.desafiodigital.views;

import io.github.desafiodigital.controllers.ControlaTexture;
import io.github.desafiodigital.controllers.ControlaBotao;
import io.github.desafiodigital.controllers.ControlaCarta;
import io.github.desafiodigital.controllers.ControlaDicas;
import io.github.desafiodigital.models.ConfiguracaoFase;
import io.github.desafiodigital.utils.Inicializadores;
import io.github.desafiodigital.models.NivelCarta;
import io.github.desafiodigital.utils.StringUtils;
import io.github.desafiodigital.utils.TaskExecutor;
import io.github.desafiodigital.DesafioDigital;
import io.github.desafiodigital.utils.Tempo;
import io.github.desafiodigital.utils.Draws;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

/** Tela do jogo do tipo cartas. */
public class TelaFaseCartas extends TelaFase {

    // Imagens da tela.
    private ControlaTexture fundoVermelho, fundoVerde, caixaDica;

    // Controlador dos botões.
    private ControlaBotao controlaBotaoX;

    // Controlador de dica.
    private ControlaDicas controlaDicas;

    // Controlador de cartas.
    private ArrayList<ControlaCarta> cartasClicadas;

    private NivelCarta nivel;

    public TelaFaseCartas(final DesafioDigital game) {
        super(game);

        nivel = game.jogo.obterNivelCarta();
        ConfiguracaoFase config = game.jogo.getConfigFase();

        // Embaralha as cartas.
        game.fisherYatesShuffle(nivel.getCartas());
        // Embaralha as dicas.
        game.fisherYatesShuffle(nivel.getDicas());

        cartas = Inicializadores.inicializaControlaCartas(
            nivel.getCartas(),
            "Cartas/carta.png", "Cartas/carta.png", 
            config.getRow(), config.getCol(), 
            280f, 320f,
            config.getMarginX(), config.getMarginY(),
            config.getSpacingX(), config.getSpacingY()
        );
        
        // Adiciona borda colorida se a fase permitir.
        setBolderColors(config.isBolderColor());

        // Inicialização da caixa de mensagem.
        caixaMensagem = Inicializadores.inicializaControlaTexture(
            "Caixas/caixaExplicacao.png", 
            this.game.getWorldWidth()/2 - 1850.0f/2, 42.0f, 1850.0f, 400.0f
        );

        // Inicialização do fundo.
        fundoVermelho = Inicializadores.inicializaControlaTexture(
            "Backgrounds/backgroundVermelho.png", 
            0, 0, this.game.getWorldWidth(), this.game.getWorldHeight());

        fundoVerde = Inicializadores.inicializaControlaTexture(
            "Backgrounds/backgroundVerde.png", 
            0, 0, this.game.getWorldWidth(), this.game.getWorldHeight());

        caixaDica = Inicializadores.inicializaControlaTexture(
            "Caixas/caixaDica.png", 100.f, 400.0f, 300.0f, 100.0f);
    
        // Inicialização do botão.
        controlaBotaoX = Inicializadores.inicializaControlaBotao(
            "Botoes/botaoX.png", 1780.0f, 380.0f, 120.0f, 120.0f);

        // Inicialização do controlador de dicas.
        controlaDicas = Inicializadores.inicializaControlaDicas(
            5, 
            "Botoes/setaEsquerda.png", 
            "Botoes/setaDireita.png",
            "Mascote/mascoteDicaApagado.png",
            "Mascote/mascoteDicaLampada.png");
    }

    @Override
    public void dispose() {
        // Libera os assets.
        super.dispose();
        this.caixaDica.dispose();
        this.fundoVerde.dispose();
        this.controlaDicas.dispose();
        this.fundoVermelho.dispose();
        this.controlaBotaoX.dispose();
        if (cartasClicadas != null) for(ControlaCarta carta: cartasClicadas) carta.dispose();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        //################################# DRAWS #########################################//
        // Desenha o botão de lâmpada.
        controlaDicas.drawLampada(game.spritebatch);
        //#################################################################################//
        executaEstado();
    }

    public void renderPadrao() {
        super.renderPadrao();
        //################################# INPUT #########################################//
        // Faz alguma coisa quando clica ou toca a tela.
        if (Gdx.input.justTouched()) { 
            game.touchedBotaoMusica();
            // Verifica se a lâmpada foi clicada.
            if (game.touchedBotao(controlaDicas.getControlaBotaoLampada())) {
                // Mostra uma dica, como o erro.
                controlaDicas.somaDica();
                estadoAtual = Estado.PROCESSANDO;
                Tempo.executarComDelay(0.3f, () -> {
                    estadoAtual = Estado.ERRO;
                });
            }

            // Verifica se alguma carta foi clicada.
            else if (touchedCarta()) {
                estadoAtual = Estado.PROCESSANDO;
                Tempo.executarComDelay(0.2f, () -> {
                
                    cartasClicadas=  cartas.cartasClicked(game.getAcertoSound(), game.getErroSound());
                    if (cartasClicadas != null) {

                        // Se retorna duas cartas é porque foi um acerto de combinação.
                        if (cartasClicadas.size() == 2) {
                            estadoAtual = Estado.PROCESSANDO;
                            Tempo.executarComDelay(0.8f, () -> {
                                estadoAtual = Estado.ACERTO;
                            });
                        }
                        // Se retorna uma carta é porque foi uma seleção de carta já fixa.
                        else if (cartasClicadas.size() == 1) {
                            estadoAtual = Estado.PROCESSANDO;
                            Tempo.executarComDelay(0.2f, () -> {
                                estadoAtual = Estado.SELECAO;
                            });
                        }
                        // Se retorna nenhuma carta, mas não nulo, é um erro de combinação.
                        else if (cartasClicadas.size() == 0) {
                            game.getNivelPartida().somaErro();
                            if ((game.getNivelPartida().getQtdErros() % 2 == 0) && (controlaDicas.somaDica())) {
                                estadoAtual = Estado.PROCESSANDO;
                                Tempo.executarComDelay(0.8f, () -> {
                                    estadoAtual = Estado.ERRO;
                                });
                            }
                            else estadoAtual = estadoPadrao;
                        }
                    }
                    else estadoAtual = estadoPadrao;
                });
            } 
        }

        //############################# MOVIMENTO #########################################//
        // Faz o movimento do botão de lâmpada.
        mudaBotao(controlaDicas.getControlaBotaoLampada());
        game.movementBotao(controlaDicas.getControlaBotaoLampada());
        //#################################################################################//
    }

    public void renderErro() {
        //################################# INPUT #########################################//
        if (Gdx.input.justTouched()) { 
            // Verifica de o botão x foi clicado.
            if (game.touchedBotao(controlaBotaoX))
                estadoAtual = estadoPadrao;

            // Verfica se o boão direito foi clicado.
            else if (game.touchedBotao(controlaDicas.getControlaBotaoDireito())) 
                controlaDicas.passaDicaDireita();

            // Verifica se o botão esquerdo foi clicado.
            else if (game.touchedBotao(controlaDicas.getControlaBotaoEsquerdo()))
                controlaDicas.passaDicaEsquerda();
        }

        //################################# DRAWS #########################################//
        String dica = StringUtils.addQuebraLinhaComLimite(
            nivel.obterDica(controlaDicas.getWhichDica()), 
            60
        );

        // Desenha um fundo transparente.
        Draws.drawFundo(
            game.spritebatch, viewport,
            fundoVermelho
        );

        // Desenha uma caixa de mensagem para mostrar a dica.
        Draws.drawMensagem(
            game.spritebatch, viewport, game.fonte,
            caixaMensagem, controlaBotaoX, 
            dica, Color.BLACK, 
            185, 350, 1.1f, 1.3f
        );

        caixaDica.draw(game.spritebatch);
        game.fonte.draw("DICA " + (controlaDicas.getWhichDica() + 1), 
            160.f, 470.0f, Color.WHITE, 1.2f, 
            Color.BLACK, 1.0f);

        // Desenha os botões de direita e esquerda.
        controlaDicas.drawBotoes(game.spritebatch);
        
        //############################# MOVIMENTO #########################################//
        // Faz o movimento dos botões se o mouse estiver em cima deles.
        game.movementBotao(controlaBotaoX);
        game.movementBotao(controlaDicas.getControlaBotaoDireito());
        game.movementBotao(controlaDicas.getControlaBotaoEsquerdo());
        //################################################################################//
    }

    public void renderAcerto() {
        //################################# INPUT #########################################//
        if (Gdx.input.justTouched()) { 
            // Verifica de o botão x foi clicado.
            if (game.touchedBotao(controlaBotaoX)) {
                if (cartas.isWin()) {
                    estadoPadrao = Estado.FINAL;
                    // Pausa a cotagem de tempo.
                    Tempo.pause();
                    // Salva o tempo gasto.
                    game.getNivelPartida().setTempo(Tempo.getElapsedTime());
                    // Calcula a pontuação final.
                    game.calculaPontuacao();
                    // Manda para o banco de dados.
                    TaskExecutor.executeTask("registraNivel");
                }
                estadoAtual = estadoPadrao;
            }
        }

        //################################# DRAWS #########################################//
        // Desenha um fundo transparente.
        Draws.drawFundo(
            game.spritebatch, viewport,
            fundoVerde
        );

        // Desenha uma caixa de explicação e as cartas clicadas.
        Draws.drawExplicacao(
            game.spritebatch, viewport, game.fonte, false,
            cartasClicadas, caixaMensagem, controlaBotaoX, Color.BLACK
        );

        //############################# MOVIMENTO #########################################//
        // Faz o movimento do botão x se o mouse estiver em cima dele.
        game.movementBotao(controlaBotaoX);
        //#################################################################################//
    }

    public void renderSelecao() {
        //################################# INPUT #########################################//
        if (Gdx.input.justTouched()) { 
            // Verifica de o botão x foi clicado.
            if (game.touchedBotao(controlaBotaoX)) {
                estadoAtual = estadoPadrao;
                cartas.restauraCartas(cartasClicadas);
            }
        }

        //################################# DRAWS #########################################//
        // Desenha um fundo transparente.
        Draws.drawFundo(
            game.spritebatch, viewport,
            fundoVerde
        );

        // Desenha uma caixa de explicação e as cartas clicadas.
        Draws.drawExplicacao(
            game.spritebatch, viewport, game.fonte, true,
            cartasClicadas, caixaMensagem, controlaBotaoX, Color.BLACK
        );

        //############################# MOVIMENTO #########################################//
        // Faz o movimento do botão x se o mouse estiver em cima dele.
        game.movementBotao(controlaBotaoX);
        //################################################################################//
    }

    public void renderFinal() {
        super.renderFinal();
        //################################# INPUT #########################################//
        if (Gdx.input.justTouched()) { 
            game.touchedBotaoMusica();
            // Se tiver acabado o jogo e aperta o botão confirmar.
            if (game.touchedBotao(controlaBotaoConfirmar)) {
                // Mudar para a próxima tela.
                game.setScreen(new TelaPontuacao(game));
                dispose(); 
            }
            // Verifica se a lâmpada foi clicada.
            else if (game.touchedBotao(controlaDicas.getControlaBotaoLampada())) {
                // Mostra uma dica, como o erro.
                controlaDicas.somaDica();
                estadoAtual = Estado.PROCESSANDO;
                Tempo.executarComDelay(0.2f, () -> {
                    estadoAtual = Estado.ERRO;
                });
            }

            // Verifica se alguma carta foi clicada.
            else if (touchedCarta()) {
                cartasClicadas=  cartas.cartasClicked(game.getAcertoSound(), game.getErroSound());
                if (cartasClicadas != null) {

                    // Se retorna uma carta é porque foi uma seleção de carta já fixa.
                    if (cartasClicadas.size() == 1) {
                        estadoAtual = Estado.PROCESSANDO;
                        Tempo.executarComDelay(0.3f, () -> {
                            estadoAtual = Estado.SELECAO;
                        });
                    }
                }
            } 
        }

        //############################# MOVIMENTO #########################################//
        // Faz o movimento do botão de lâmpada.
        mudaBotao(controlaDicas.getControlaBotaoLampada());
        game.movementBotao(controlaDicas.getControlaBotaoLampada());
        //#################################################################################//
    }

    @Override
    public String toString() {
        return "FaseCartasScreen"; // Identificador da tela.
    }
}
