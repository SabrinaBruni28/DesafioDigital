package com.badlogic.desafiodigital.utils;

import com.badlogic.desafiodigital.controllers.ControlaTexture;
import com.badlogic.desafiodigital.controllers.ControlaBotao;
import com.badlogic.desafiodigital.controllers.ControlaCarta;
import com.badlogic.desafiodigital.controllers.ControlaFonte;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;

/** Classe para desenhar na tela padrões específicos e repetitivos. */
public abstract class Draws {

    // Configuração inicial do batch com viewport
    public static void setupBatch(SpriteBatch batch, Viewport viewport) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
    }

    public static void drawMensagem(
        SpriteBatch batch,
        Viewport viewport,
        ControlaFonte fonte,
        ControlaTexture caixa,
        ControlaBotao botao, 
        String mensagem, Color cor, 
        float xFonte, float yFonte, float widthFonte, float heightFonte) {

        setupBatch(batch, viewport);
        caixa.draw(batch);
        botao.draw(batch);
        fonte.draw(mensagem, xFonte, yFonte, cor, widthFonte, heightFonte);
    }

    public static void drawExplicacao(
            SpriteBatch batch,
            Viewport viewport,
            ControlaFonte fonte,
            boolean umaCarta,
            ArrayList<ControlaCarta> cartas,
            ControlaTexture caixa,
            ControlaBotao botao, 
            Color cor) {

        setupBatch(batch, viewport);
        caixa.draw(batch);
        botao.draw(batch);

        // Seleciona a função apropriada para desenhar cartas com base na quantidade
        drawCartas(batch, viewport, fonte, cartas, umaCarta ? 1 : 2);
        
        String frase = StringUtils.addQuebraLinhaComLimite(
            StringUtils.justificarTexto(
                cartas.get(0).getCarta().getDescricao(), 
                80
            ), 
            80
        );

        float fonteWidth = caixa.getSprite().getWidth() * 0.77f;
        float fonteHeight = caixa.getSprite().getHeight() * 0.80f;

        // Calcula a posição inicial do texto dentro da caixa
        float fonteX = caixa.getSprite().getX() + 200;
        float fonteY = caixa.getSprite().getY() + 30; // Começa no topo da caixa

        // Ajusta o texto para caber dentro do espaço definido
        desenhaTextoProporcional(fonte, frase, cor, fonteX, fonteY, fonteWidth, fonteHeight, 1.2f);
    }

    private static void drawCartas(
            SpriteBatch batch, Viewport viewport, ControlaFonte fonte, 
            ArrayList<ControlaCarta> cartas, int quantidadeCartas) {
        
        float[] xPosicoes = quantidadeCartas == 1 ? 
            new float[] {700.0f} : new float[] {330.0f, 1080.0f};
        float yPosicao = 450.0f;
        
        for (int i = 0; i < quantidadeCartas; i++) {
            drawCartaImagemNome(batch, viewport, fonte, cartas.get(i), xPosicoes[i], yPosicao, 1.7f);
        }
    }

    public static void drawCartaImagemNome(
            SpriteBatch batch, Viewport viewport, ControlaFonte fonte, 
            ControlaCarta carta, float x, float y, float scaleFactor) {

        String nome = StringUtils.addQuebraLinhaComLimiteCentralizado(
            carta.getCarta().getNome(), 12
        );
        setupBatch(batch, viewport);

        float cardWidth = carta.getBounds().width * scaleFactor;
        float cardHeight = carta.getBounds().height * scaleFactor;

        float dispositivoWidth = cardWidth * 0.6f;
        float dispositivoHeight = cardHeight * 0.5f;
        float dispositivoX = x + (cardWidth - dispositivoWidth) / 2;
        float dispositivoY = y + cardHeight - dispositivoHeight - 15;

        float fonteWidth = cardWidth * 0.8f;
        float fonteHeight = cardHeight * 0.5f;
        float fonteX = x + (cardWidth - fonteWidth) / 2;
        float fonteY = y + (cardHeight - fonteHeight) / 2 - 180;

        batch.begin();
            batch.draw(carta.getFrontTexture(), x, y, cardWidth, cardHeight);
            desenhaTextureProporcional(batch, carta.getDispositivoTexture(), dispositivoX, dispositivoY, dispositivoWidth, dispositivoHeight);
        batch.end();
        desenhaTextoProporcional(fonte, nome, Color.BLACK, fonteX, fonteY, fonteWidth, fonteHeight, Math.min (1.1f, 1.1f * 28 / nome.length()));
    }

    public static void drawCartaExplicacao(
            SpriteBatch batch, Viewport viewport, ControlaFonte fonte, 
            ControlaCarta carta, ControlaBotao botao, float x, float y, float scaleFactor) {

        String descricao = carta.getCarta().getNome() + 
            ":\n\n" + 
            StringUtils.addQuebraLinhaComLimite(
                StringUtils.justificarTexto(
                    carta.getCarta().getDescricao(), 
                    30
                ),
                30
            );

        setupBatch(batch, viewport);

        float cardWidth = carta.getBounds().width * scaleFactor;
        float cardHeight = carta.getBounds().height * scaleFactor;
        Texture cartaExplicacao = Inicializadores.inicializaTexture("Cartas/cartaExplicacao.png");

        float fonteWidth = cardWidth * 0.70f;
        float fonteHeight = cardHeight * 0.90f;
        float fonteX = x + 100;
        float fonteY = y + 120;
        
        batch.begin();
            batch.draw(cartaExplicacao, x, y, cardWidth, cardHeight);
        batch.end();
        botao.draw(batch);
        desenhaTextoProporcional(fonte, descricao, Color.BLACK, fonteX, fonteY, fonteWidth, fonteHeight, 1.1f);
    }

    public static void drawFundo(
        SpriteBatch batch,
        Viewport viewport,
        ControlaTexture fundo) {

        setupBatch(batch, viewport);
        fundo.draw(batch);
    }

    public static void desenhaTextureProporcional(SpriteBatch batch, Texture texture, float xEspaco, float yEspaco, float larguraEspaco, float alturaEspaco) {
        // Obtém dimensões da textura original
        float larguraOriginal = texture.getWidth();
        float alturaOriginal = texture.getHeight();
    
        // Calcula os fatores de escala para largura e altura
        float escalaLargura = larguraEspaco / larguraOriginal;
        float escalaAltura = alturaEspaco / alturaOriginal;
    
        // Usa o menor fator de escala para manter a proporção
        float escala = Math.min(escalaLargura, escalaAltura);
    
        // Calcula as dimensões finais
        float larguraFinal = larguraOriginal * escala;
        float alturaFinal = alturaOriginal * escala;
    
        // Calcula a posição para centralizar a textura no espaço
        float xFinal = xEspaco + (larguraEspaco - larguraFinal) / 2;
        float yFinal = yEspaco + (alturaEspaco - alturaFinal) / 2;
    
        // Desenha a textura com as novas dimensões
        batch.draw(texture, xFinal, yFinal, larguraFinal, alturaFinal);
    }    

    public static void desenhaTextoProporcional(
            ControlaFonte fonte, String texto, Color cor,
            float xEspaco, float yEspaco, float larguraEspaco, float alturaEspaco,
            float fatorEscala) {

        // Configura o GlyphLayout para medir o texto
        GlyphLayout layout = new GlyphLayout();
        BitmapFont bitFont = fonte.getFont();

        // Aplica a escala inicial na fonte
        bitFont.getData().setScale(1.0f); // Garante que a escala inicial é 1
        layout.setText(bitFont, texto);

        // Obtém as dimensões do texto original
        float larguraTexto = layout.width;
        float alturaTexto = layout.height;

        // Calcula os fatores de escala para largura e altura
        float escalaLargura = larguraEspaco / larguraTexto;
        float escalaAltura = alturaEspaco / alturaTexto;

        // Usa o menor fator de escala para manter a proporção e aplica o fator de ajuste
        float escala = Math.min(escalaLargura, escalaAltura) * fatorEscala;

        // Aplica a escala calculada na fonte
        bitFont.getData().setScale(escala);

        // Recalcula o layout com a nova escala
        layout.setText(bitFont, texto);

        // Calcula a posição para centralizar o texto no espaço
        float xFinal = xEspaco + (larguraEspaco - layout.width) / 2;
        float yFinal = yEspaco + (alturaEspaco + layout.height) / 2; // Ajusta para alinhar o baseline corretamente

        // Desenha o texto no local calculado
        fonte.draw(texto, xFinal, yFinal, cor, escala);

        // Reseta a escala para evitar impacto em outros textos
        bitFont.getData().setScale(1.0f);
    }

    public static void desenhaTextoProporcional(
            ControlaFonte fonte, String texto, Color cor,
            float xEspaco, float yEspaco, float larguraEspaco, float alturaEspaco,
            float fatorEscala, Color corContorno, float contorno) {

        // Configura o GlyphLayout para medir o texto
        GlyphLayout layout = new GlyphLayout();
        BitmapFont bitFont = fonte.getFont();

        // Aplica a escala inicial na fonte
        bitFont.getData().setScale(1.0f); // Garante que a escala inicial é 1
        layout.setText(bitFont, texto);

        // Obtém as dimensões do texto original
        float larguraTexto = layout.width;
        float alturaTexto = layout.height;

        // Calcula os fatores de escala para largura e altura
        float escalaLargura = larguraEspaco / larguraTexto;
        float escalaAltura = alturaEspaco / alturaTexto;

        // Usa o menor fator de escala para manter a proporção e aplica o fator de ajuste
        float escala = Math.min(escalaLargura, escalaAltura) * fatorEscala;

        // Aplica a escala calculada na fonte
        bitFont.getData().setScale(escala);

        // Recalcula o layout com a nova escala
        layout.setText(bitFont, texto);

        // Calcula a posição para centralizar o texto no espaço
        float xFinal = xEspaco + (larguraEspaco - layout.width) / 2;
        float yFinal = yEspaco + (alturaEspaco + layout.height) / 2; // Ajusta para alinhar o baseline corretamente

        // Desenha o texto no local calculado
        fonte.draw(texto, xFinal, yFinal, cor, escala, corContorno, contorno);

        // Reseta a escala para evitar impacto em outros textos
        bitFont.getData().setScale(1.0f);
    }
}