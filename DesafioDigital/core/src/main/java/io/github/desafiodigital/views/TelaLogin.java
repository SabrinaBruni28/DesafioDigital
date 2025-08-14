package io.github.desafiodigital.views;

import io.github.desafiodigital.controllers.ControlaCaixaTexto;
import io.github.desafiodigital.controllers.ControlaTexture;
import io.github.desafiodigital.controllers.ControlaBotao;
import io.github.desafiodigital.utils.Inicializadores;
import io.github.desafiodigital.utils.StringUtils;
import io.github.desafiodigital.utils.TaskExecutor;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.desafiodigital.utils.Arquivos;
import io.github.desafiodigital.DesafioDigital;
import io.github.desafiodigital.utils.Tempo;
import io.github.desafiodigital.utils.Draws;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import java.util.HashMap;

/** Tela de Login, onde o usuário escreve seus dados. */
public class TelaLogin implements Screen {

    final DesafioDigital game;

    // Define as proporções da janela, a "câmera" que será visualizada.
    public FitViewport viewport;

    // Imagens da tela.
    private ControlaTexture background, mascote, mascoteNormal, mascoteErro, maoMascote, caixaLogin, caixaMensagem;

    // Caixa de texto.
    private ControlaCaixaTexto caixasTexto;

    // Controladores dos botões.
    private ControlaBotao controlaBotaoConfirmar, controlaBotaoX;

    // Define os estados da tela.
    private enum Estado {PADRAO, ERRO};

    // Define o render de cada estado.
    protected HashMap<Estado, Runnable> renders;

    // Define o estado atual.
    private Estado estadoAtual;

    // Para mensagem de erro.
    private String fraseErro;

    public TelaLogin(final DesafioDigital game) {
        this.game = game;
        inicializaEstados();
        
        // Inicializa e define o tamanho do cenário.
		viewport = Inicializadores.inicializaViewport(this.game.getWorldWidth(), this.game.getWorldHeight());
        
        // Inicialização das imagens. 
        background = Inicializadores.inicializaControlaTexture(
            "Backgrounds/backgroundInicial.png", 0, 0, this.game.getWorldWidth(), this.game.getWorldHeight());
        
        mascoteNormal = Inicializadores.inicializaControlaTexture(
            "Mascote/mascote.png", this.game.getWorldWidth()/2 - 600.0f/2, 100.0f , 600.0f, 800.0f);
        
        mascoteErro = Inicializadores.inicializaControlaTexture(
            "Mascote/mascoteErro.png", this.game.getWorldWidth()/2 - 600.0f/2,100.0f , 600.0f, 800.0f);
        
        // Mascote inicial.
        mascote = mascoteNormal;

        maoMascote = Inicializadores.inicializaControlaTexture(
            "Mascote/maoMascote.png", this.game.getWorldWidth()/2 - 650.0f/2, 565.0f, 650.0f, 120.0f);
        
        caixaLogin = Inicializadores.inicializaControlaTexture(
            "Caixas/caixaLogin.png", this.game.getWorldWidth()/2 - 800.0f/2, 50.0f, 800.0f, 580.0f);
        
        caixaMensagem = Inicializadores.inicializaControlaTexture(
            "Caixas/caixaMensagem.png", this.game.getWorldWidth()/2 - 905.0f/2, 42.0f, 905.0f, 265.0f);

        // Inicialização da caixa texto.
        caixasTexto = Inicializadores.inicializaControlaCaixaTexto(
            Arquivos.getArquivoJson(), Arquivos.getArquivoFnt(), Arquivos.getArquivoPng(), 
            2, viewport, this.game.getClickSound());

        // Atribuição de valores para cada caixa.
        caixasTexto.setTamanhos(0, 630.0f, 380.0f, 680.0f, 70.0f);
        caixasTexto.setMessageText("DIGITE SEU NOME", 0);
        caixasTexto.setLimiteCaracteres(0, 50);

        caixasTexto.setTamanhos(1, 630.0f, 180.0f, 680.0f, 70.0f);
        caixasTexto.setMessageText("DIGITE SUA TURMA", 1);
        caixasTexto.setLimiteCaracteres(1, 25);

        // Inicialização dos botões.
        controlaBotaoConfirmar = Inicializadores.inicializaControlaBotao(
            "Botoes/botaoConfirmar.png", 1150.0f, 30.0f, 280.0f, 130.0f);

        controlaBotaoX = Inicializadores.inicializaControlaBotao(
            "Botoes/botaoX.png", 1320.0f, 220.0f, 100.0f, 100.0f);
    }

    @Override
    public void dispose() {
        // Libera os assets.
        this.background.dispose();

        this.mascote.dispose();
        this.maoMascote.dispose();
        this.mascoteErro.dispose();
        this.mascoteNormal.dispose();

        this.caixaLogin.dispose();
        this.caixaMensagem.dispose();

        this.caixasTexto.dispose();

        this.controlaBotaoX.dispose();
        this.controlaBotaoConfirmar.dispose();
    }

    @Override
    public void show() {
        /* */
    }

    @Override
    public void hide() {
        /* */
    }

     public void inicializaEstados() {
        // Inicializa o estado.
        estadoAtual = Estado.PADRAO;
        renders = new HashMap<Estado, Runnable>();
        renders.put(Estado.ERRO, this :: renderErro);
        renders.put(Estado.PADRAO, this :: renderPadrao);
    }

    public void executaEstado() {
        // Recupera a função a ser executada de acordo com o estado atual.
        Runnable acao = renders.get(estadoAtual);
        // Executa a função se for um estado válido.
        if (acao != null) acao.run();
    }

    public void renderErro() {
        mascote = mascoteErro;
        // Para o mascote de erro onde o mascote normal estava.
        mascoteErro.getSprite().setY(mascoteNormal.getSprite().getY());
        caixasTexto.desativaCaixas(true);

        if (Gdx.input.justTouched()) { 
            // Verifica de o botão x foi clicado.
            if (game.touchedBotao(controlaBotaoX)) estadoAtual = Estado.PADRAO;
        }

        Draws.drawMensagem(
            game.spritebatch, viewport, game.fonte,
            caixaMensagem, controlaBotaoX, 
            fraseErro, Color.BLACK, 
            550, 188, 1.37f * 25/fraseErro.length(), 1.8f
        );
                        
        // Faz o movimento do botão x se o mouse estiver em cima dele.
        controlaBotaoX.movementBotao(
            game.getMouseX(),game.getMouseY(), 1.2f, game.getHoverSound()
        );
    }

    public void renderPadrao() {
        mascote = mascoteNormal;
        caixasTexto.desativaCaixas(false);

        // Faz alguma coisa quando clica ou toca a tela.
        if (Gdx.input.justTouched()) { 
            game.touchedBotaoMusica();
            // Verifica se o botão confirmar foi clicado. 
            if (game.touchedBotao(controlaBotaoConfirmar)) {
                // Confere as entradas.
                if (confereNome() && confereTurma()) {
                        // Salva as entradas se forem válidas.
                        game.setNome(caixasTexto.getTexto(0).trim());
                        game.setTurma(caixasTexto.getTexto(1).trim());
                        TaskExecutor.executeTask("iniciaPartida");
                        
                        // Mudar para a próxima tela.
                        game.setScreen(new TelaIntroducaoFase(game));
                        dispose(); 
                }
                else {
                    Tempo.executarComDelay(0.1f, () -> {
                        estadoAtual = Estado.ERRO;
                        game.getErroSound().play();
                    });
                }
            }
        }

        game.movementBotao(game.getControlaBotaoMusica());
        // Movimenta o mascote enquanto não tem erro.
        mascote.movimentY(80, 100, 200);
        // Faz o movimento do botão confirmar se o mouse estiver em cima dele.
        controlaBotaoConfirmar.movementBotao(
            game.getMouseX(),game.getMouseY(), 1.2f, game.getHoverSound()
        );    
    }

    @Override
    public void render(float delta) {
        input();
        draw();
        executaEstado();
    }

    private void input() {
        // Pega onde o toque aconteceu na tela.
        game.setMouse(); 
        // Converte as unidades da janela para as coordenadas do cenário.
        viewport.unproject(game.touchPos);
    }

    private void draw() {
        // Limpa a tela.
        ScreenUtils.clear(Color.BLACK);
        Draws.setupBatch(game.spritebatch, viewport);
    
        // Desenha o background.
        background.draw(game.spritebatch);

        // Desenha o mascote.
        mascote.draw(game.spritebatch);
        
        // Desenha a caixa.
        caixaLogin.draw(game.spritebatch);
        
        // Desenha a mão do mascote.
        maoMascote.draw(game.spritebatch);
    
        // Desenha o botão.
        controlaBotaoConfirmar.draw(game.spritebatch);
        game.getControlaBotaoMusica().draw(game.spritebatch);

        // Desenha as caixas de texto.
        caixasTexto.draw();

        // Desenha os textos.
        game.fonte.draw("NOME:", 
            630, 505, Color.WHITE, 1.5f, Color.BLACK, 1.0f);
        game.fonte.draw("TURMA:", 
            630, 305,  Color.WHITE, 1.5f, Color.BLACK, 1.0f);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        caixasTexto.resize(width, height);
        game.fonte.resize( Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        return "LoginScreen"; // Identificador da tela.
    }

    public boolean confereNome() {
        // Pega o texto escrito na caixa.
        String texto = caixasTexto.getTexto(0);

        // Se não escrever nada ou escrever números ou escrever caracteres especiais é inválido.
        if (StringUtils.isNulo(texto) || StringUtils.isVazio(texto)) {
            fraseErro = "O NOME NÃO PODE ESTAR VAZIO"; // 27 letras
            return false;
        }

        if (!StringUtils.hasPeloMenosNCaracteres(texto, 3)) {
            fraseErro = "O NOME DEVE TER PELO MENOS TRÊS LETRAS"; // 38 letras
            return false;
        }

        if (StringUtils.hasCaracteresEspeciais(texto) || StringUtils.hasNumeros(texto)) {
            fraseErro = "O NOME SÓ PODE TER LETRAS"; // 25 letras
            return false;
        }

        // Se não contém nenhum dos caracteres inválidos é válido.
        return true;
    }

    public boolean confereTurma() {
        // Pega o texto escrito na caixa.
        String texto = caixasTexto.getTexto(1);
        
        // Se não escrever nada ou caracteres especiais é inválido.
        if(StringUtils.isNulo(texto) || StringUtils.isVazio(texto)) {
            fraseErro = "A TURMA NÃO PODE ESTAR VAZIA"; // 28 letras
            return false;
        }
        if (StringUtils.hasCaracteresEspeciais(texto)) {
            fraseErro = "A TURMA SÓ PODE TER LETRAS OU NÚMEROS"; // 37 letras
            return false;
        }

        // Se não contém nenhum dos caracteres inválidos é válido.
        return true;
    }
}
