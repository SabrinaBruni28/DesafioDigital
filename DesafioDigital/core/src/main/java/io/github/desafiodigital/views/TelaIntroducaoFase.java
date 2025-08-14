package io.github.desafiodigital.views;

import io.github.desafiodigital.controllers.ControlaTexture;
import io.github.desafiodigital.controllers.ControlaBotao;
import io.github.desafiodigital.utils.Inicializadores;
import io.github.desafiodigital.utils.StringUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.desafiodigital.DesafioDigital;
import io.github.desafiodigital.utils.Draws;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;

/** Tela de introdução a Fase, contendo uma explicação sobre ela. */
public class TelaIntroducaoFase implements Screen {

    final DesafioDigital game;

    // Define as proporções da janela, a "câmera" que será visualizada.
    public FitViewport viewport;

    // Imagens da tela.
    private ControlaTexture background, mascote, caixaExplicacao;

    // Controladores dos botões.
    private ControlaBotao controlaBotaoConfirmar;

    public TelaIntroducaoFase(final DesafioDigital game) {
        this.game = game;

        // Inicializa e define o tamanho do cenário.
		viewport = Inicializadores.inicializaViewport(this.game.getWorldWidth(), this.game.getWorldHeight());
        
        // Inicialização das imagens. 
        background = Inicializadores.inicializaControlaTexture(
            "Backgrounds/backgroundFases.png", 10f, 10f, this.game.getWorldWidth() - 20, this.game.getWorldHeight() - 10);

        String mascoteString = game.jogo.getConfigFase().getImagemMascote();
        mascote = Inicializadores.inicializaControlaTexture(
            mascoteString, 300f, 80f, 478.0f, 490.17f);

        // Rotaciona o mascote.
        mascote.getSprite().setOrigin(0, 0);
        mascote.getSprite().setRotation(42.85f);

        caixaExplicacao = Inicializadores.inicializaControlaTexture(
            "Caixas/caixaIntroducao.png", this.game.getWorldWidth()/2 - 1100.0f/2, this.game.getWorldHeight()/2 - 900.0f/1.9f, 1100.0f, 900.0f);
            
        // Inicialização dos botões.
        controlaBotaoConfirmar = Inicializadores.inicializaControlaBotao(
            "Botoes/botaoConfirmar.png", 1300.0f, 40.0f, 290.0f, 140.0f);
    }

    @Override
    public void dispose() {
        // Libera os assets.
        this.mascote.dispose();
        this.background.dispose();
        this.caixaExplicacao.dispose();
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
            // Verifica se o botão confirmar foi clicado. 
            if (game.touchedBotao(controlaBotaoConfirmar)) {
                // Mudar para a próxima tela.
                game.setScreen(game.jogo.decideNextNivel(game));
                dispose(); 
            }
        }
        // Faz o movimento do botão se o mouse estiver em cima dele.
        game.movementBotao(game.getControlaBotaoMusica());
        game.movementBotao(controlaBotaoConfirmar);
    }

    private void draw() {
        // Limpa a tela.
        ScreenUtils.clear(game.getCorFundo());
        Draws.setupBatch(game.spritebatch, viewport);

        // Desenha o background.
        background.draw(game.spritebatch);
        // Desenha mascote.
        mascote.draw(game.spritebatch);
        // Desenha o retângulo.
        caixaExplicacao.draw(game.spritebatch);
        // Desenha o botão.
        controlaBotaoConfirmar.draw(game.spritebatch);
        game.getControlaBotaoMusica().draw(game.spritebatch);

        // Desenha qual a fase.
        game.fonte.draw("FASE " + game.jogo.getFaseAtual(), 
            855, 1050, Color.WHITE, 1.5f, Color.BLACK, 1.0f);
        
        /* A frase estará na classe da fase referente */
        String frase = StringUtils.addQuebraLinhaEmPontos(
            StringUtils.addQuebraLinhaComLimite(
                StringUtils.justificarTexto(
                    game.jogo.obterFaseAtual().getExplicacao(),
                    38
                ),
                38
            )
        );
        // Desenha a explicação.
        game.fonte.draw(frase, 460, 850, Color.BLACK, 1.1f, Math.min (1.1f, 1.1f * 300/frase.length()));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        game.fonte.resize( Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void pause() {
        // 
    }

    @Override
    public void resume() {
        // 
    }

    @Override
    public String toString() {
        return "IntroduçãoFaseScreen"; // Identificador da tela.
    }
}
