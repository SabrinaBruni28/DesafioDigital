package io.github.DesafioDigital.utils;

import io.github.DesafioDigital.controllers.ControlaCaixaTexto;
import io.github.DesafioDigital.controllers.ControlaTexture;
import io.github.DesafioDigital.controllers.ControlaCenario;
import io.github.DesafioDigital.controllers.ControlaCartas;
import io.github.DesafioDigital.controllers.ControlaBotao;
import io.github.DesafioDigital.controllers.ControlaDicas;
import io.github.DesafioDigital.controllers.ControlaCarta;
import io.github.DesafioDigital.controllers.ControlaFonte;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.DesafioDigital.components.Botao;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.DesafioDigital.models.Cenario;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.DesafioDigital.models.Carta;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

/** Classe para inicializar corretamente classes que dependem de outras. */
public abstract class Inicializadores {

    public static ControlaFonte inicializaControlaFonte(SpriteBatch batch, String arquivoFnt, String arquivoPng, float width, float height) {
        // Inicialização do viewport.
        Viewport view = Inicializadores.inicializaViewport(width, height);
        
        // Carrega a fonte a partir dos arquivos.
        BitmapFont font = new BitmapFont(Gdx.files.internal(arquivoFnt), Gdx.files.internal(arquivoPng), false);
        
        // Inicialização da fonte.
        return new ControlaFonte(batch, view, font);
    }

    public static ControlaCaixaTexto inicializaControlaCaixaTexto(
            String arquivoJson, String arquivoFnt, String arquivoPng, int qntCaixa, Viewport view, Sound tecla) {
        
        // Carrega um skin básico.
        Skin skin = new Skin(Gdx.files.internal(arquivoJson));
        Stage stage = new Stage(new ScreenViewport());

        // Inicialização da caixa de texto.
        ControlaCaixaTexto caixasTexto= new ControlaCaixaTexto(qntCaixa, skin, stage, view);

        // Configura e adiciona os TextFields à stage.
        caixasTexto.setupTextFields(arquivoFnt, arquivoPng, tecla);

        return caixasTexto;
    }

    public static ControlaBotao inicializaControlaBotao(
            String botaoTexture, String outroBotao, float x, float y, float width, float height) {

        // Inicialização da imagem do botão. 
        Texture botaoTextureCriado = inicializaTexture(botaoTexture);
        Texture outroBotaoTextureCriado = inicializaTexture(outroBotao);
                  
        // Inicialização do retângulo.
        Rectangle retangulo = new Rectangle(x, y, width, height);

        // Inicialização do botão
        Botao botao = new Botao(botaoTextureCriado, retangulo);
        botao.setOutroTexture(outroBotaoTextureCriado);

        // Inicialização do controlador.
        return new ControlaBotao(botao);
    }

    public static ControlaBotao inicializaControlaBotao(
            String botaoTexture, float x, float y, float width, float height) {

        // Inicialização da imagem do botão. 
        Texture botaoTextureCriado = inicializaTexture(botaoTexture);
                
        // Inicialização do retângulo.
        Rectangle retangulo = new Rectangle(x, y, width, height);

        // Inicialização do botão
        Botao botao = new Botao(botaoTextureCriado, retangulo);

        // Inicialização do controlador.
        return new ControlaBotao(botao);
}

    public static FitViewport inicializaViewport(float width, float height) {
        // Inicializa e define o tamanho do cenário.
		FitViewport viewport = new FitViewport( width, height );
        // Centraliza o cenário.
		viewport.getCamera().position.set( width / 2.0f, height / 2.0f, 0 ); 

        return viewport;
    }

    public static ControlaTexture inicializaControlaTexture(
            String arquivoImagem, float x, float y, float width, float height) {
        // Inicialização da imagem.
        Texture elementoTexture = inicializaTexture(arquivoImagem);
        
        // Inicialização do sprite.
        Sprite sprite = new Sprite(elementoTexture);
        sprite.setPosition(x, y); // Define a posição inicial
        sprite.setSize(width, height); // Define o tamanho inicial

        // Inicialização do controlador.
        return new ControlaTexture(elementoTexture, sprite);
    }

    public static ControlaCarta inicializaControlaCarta(
            Carta carta,
            float x, float y, float width, float height, 
            Texture back, Texture front) {

        // Inicialização do retângulo.
        Rectangle retangulo = new Rectangle(x, y, width, height);

        // Inicialização do controlador.
        return new ControlaCarta(carta, retangulo, back, front);
    }

    public static ControlaCartas inicializaControlaCartas(
            ArrayList<Carta> cartas,
            String cardBack, String cardFront, 
            int rows, int cols, 
            float cardWidth, float cardHeight,  
            float marginX, float marginY, 
            float spacingX, float spacingY) {

        // Inicialização das texturas das cartas.
        Texture frontTexture = inicializaTexture(cardFront);
        Texture backTexture = inicializaTexture(cardBack);

        // Embaralhando a lista
        //Collections.shuffle(cartas);

        // Inicialização do controlador.
        return new ControlaCartas(
            cartas,
            rows, cols, cardWidth, cardHeight, 
            marginX, marginY, spacingX, spacingY, 
            backTexture, frontTexture);
    }

    public static ControlaCenario inicializaControlaCenario(
            Cenario cenario,
            float x, float y, float width, float height, 
            String fundo) {

        // Inicialização do retângulo.
        Rectangle retangulo = new Rectangle(x, y, width, height);
        // Inicialização das texturas do cenario.
        Texture fundoTexture = inicializaTexture(fundo);

        // Inicialização do controlador.
        return new ControlaCenario(cenario, retangulo, fundoTexture);
    }

    public static ControlaDicas inicializaControlaDicas(
            int limiteDicas, 
            String botaoEsquedo, String botaoDireito, String botaoLampada) {
        // Botões para a dica.
        ControlaBotao controlaBotaoEsquerdo = Inicializadores.inicializaControlaBotao(
            botaoEsquedo, 10.0f, 200.0f, 80.0f, 80.0f);

        ControlaBotao controlaBotaoDireito = Inicializadores.inicializaControlaBotao(
            botaoDireito, 1830.0f, 210.0f, 80.0f, 80.0f);

        ControlaBotao controlaBotaoLampada = Inicializadores.inicializaControlaBotao(
            botaoLampada, 40.0f, 940.0f, 80.0f, 80.0f);

        return new ControlaDicas(limiteDicas, controlaBotaoEsquerdo, controlaBotaoDireito, controlaBotaoLampada);
    }

    public static ControlaDicas inicializaControlaDicas(
            int limiteDicas, 
            String botaoEsquedo, String botaoDireito, String botaoLampada, String outroBotaoLampada) {
        // Botões para a dica.
        ControlaBotao controlaBotaoEsquerdo = Inicializadores.inicializaControlaBotao(
            botaoEsquedo, 10.0f, 200.0f, 80.0f, 80.0f);

        ControlaBotao controlaBotaoDireito = Inicializadores.inicializaControlaBotao(
            botaoDireito, 1830.0f, 210.0f, 80.0f, 80.0f);

        ControlaBotao controlaBotaoLampada = Inicializadores.inicializaControlaBotao(
            botaoLampada, outroBotaoLampada, 40.0f, 940.0f, 80.0f, 80.0f);

        return new ControlaDicas(limiteDicas, controlaBotaoEsquerdo, controlaBotaoDireito, controlaBotaoLampada);
    }

    public static Texture inicializaTexture(String imagem) {
        Texture texture = new Texture(Gdx.files.internal(imagem), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        return texture;
    }
}
