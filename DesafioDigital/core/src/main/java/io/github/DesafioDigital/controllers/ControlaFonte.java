package io.github.DesafioDigital.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;

/** Classe para controlar textos na tela. */
public class ControlaFonte {
    // Cenário para os textos.
    private SpriteBatch Fontbatch;
    // Fonte para os textos.
    private BitmapFont font;
    // Define as proporções da janela, a "câmera" que será visualizada.
    private Viewport view;

    public ControlaFonte(SpriteBatch batch, Viewport view, BitmapFont font) {
        this.Fontbatch = batch;
        this.view = view;
        this.font = font;
        // Melhora a qualidade do texto.
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void dispose() {
        if (font != null) this.font.dispose();
        if (Fontbatch != null) this.Fontbatch.dispose();
    }

    public void resize(int width, int height) {
        view.update(width, height, true);
    }

    public void draw(String string, float x, float y, Color cor, float tamanho) {
        // Aumentar o tamanho da fonte com escala.
        font.getData().setScale(tamanho);
        // Alterando a cor da fonte.
        font.setColor(cor);

        // Atualiza a matriz de projeção.
        Fontbatch.setProjectionMatrix(view.getCamera().combined);

        // Inicia o desenho.
        Fontbatch.begin();
        // Desenha o texto.
        font.draw(Fontbatch, string, x, y);
        // Termina o desenho.
        Fontbatch.end();
    }

    public void draw(String string, float x, float y, Color cor, float width, float height) {
        // Aumentar o tamanho da fonte com escala. 
        font.getData().setScale(width, height);
        // Alterando a cor da fonte.
        font.setColor(cor);

        // Atualiza a matriz de projeção.
        Fontbatch.setProjectionMatrix(view.getCamera().combined);

        // Inicia o desenho.
        Fontbatch.begin();
        // Desenha o texto.
        font.draw(Fontbatch, string, x, y);
        // Termina o desenho.
        Fontbatch.end();
    }

    public void draw(String string, float x, float y) {
        // Aumentar o tamanho da fonte com escala. 
        font.getData().setScale(1.0f);
        // Alterando a cor da fonte. 
        font.setColor(Color.BLACK);

        // Atualiza a matriz de projeção.
        Fontbatch.setProjectionMatrix(view.getCamera().combined);

        // Inicia o desenho.
        Fontbatch.begin();
        // Desenha os textos.
        font.draw(Fontbatch, string, x, y);
        // Termina o desenho.
        Fontbatch.end();
    }

    public void draw(String texto, float x, float y, Color corFonte, Color corContorno, float contorno) {
        font.setColor(corContorno); // Cor do contorno
    
        // Inicia o desenho.
        Fontbatch.begin();
        // Desenha o texto em 8 direções ao redor para criar o efeito de contorno
        font.draw(Fontbatch, texto, x - contorno, y); // Esquerda
        font.draw(Fontbatch, texto, x + contorno, y); // Direita
        font.draw(Fontbatch, texto, x, y + contorno); // Acima
        font.draw(Fontbatch, texto, x, y - contorno); // Abaixo
        font.draw(Fontbatch, texto, x - contorno, y + contorno); // Diagonal cima-esquerda
        font.draw(Fontbatch, texto, x + contorno, y + contorno); // Diagonal cima-direita
        font.draw(Fontbatch, texto, x - contorno, y - contorno); // Diagonal baixo-esquerda
        font.draw(Fontbatch, texto, x + contorno, y - contorno); // Diagonal baixo-direita
    
        // Desenha o texto principal
        font.setColor(corFonte); // Cor do texto
        font.draw(Fontbatch, texto, x, y);

        // Inicia o desenho.
        Fontbatch.end();
    }    

    public void draw(String texto, float x, float y, Color corFonte, float tamanho, Color corContorno, float contorno) {
        font.setColor(corContorno); // Cor do contorno
        
        // Aumentar o tamanho da fonte com escala. 
        font.getData().setScale(tamanho);

        // Inicia o desenho.
        Fontbatch.begin();
    
        // Desenha o texto em 8 direções ao redor para criar o efeito de contorno
        font.draw(Fontbatch, texto, x - contorno, y); // Esquerda
        font.draw(Fontbatch, texto, x + contorno, y); // Direita
        font.draw(Fontbatch, texto, x, y + contorno); // Acima
        font.draw(Fontbatch, texto, x, y - contorno); // Abaixo
        font.draw(Fontbatch, texto, x - contorno, y + contorno); // Diagonal cima-esquerda
        font.draw(Fontbatch, texto, x + contorno, y + contorno); // Diagonal cima-direita
        font.draw(Fontbatch, texto, x - contorno, y - contorno); // Diagonal baixo-esquerda
        font.draw(Fontbatch, texto, x + contorno, y - contorno); // Diagonal baixo-direita
    
        // Desenha o texto principal
        font.setColor(corFonte); // Cor do texto
        font.draw(Fontbatch, texto, x, y);

        // Inicia o desenho.
        Fontbatch.end();
    } 
    
    public void draw(String texto, float x, float y, float tamanho, float intervalo, Color cor) {
        // Variáveis de controle estáticas para manter o estado entre chamadas
        long currentTime = System.currentTimeMillis(); // Obtém o tempo atual em milissegundos
        boolean mostrarTexto = ((currentTime / (int)(intervalo * 1000)) % 2) == 0; // Alterna entre true e false com base no intervalo
    
        if (mostrarTexto) {
            font.getData().setScale(tamanho); // Define o tamanho da fonte
            font.setColor(cor); // Define a cor do texto
    
            // Atualiza a matriz de projeção
            Fontbatch.setProjectionMatrix(view.getCamera().combined);
    
            // Inicia o desenho
            Fontbatch.begin();
            font.draw(Fontbatch, texto, x, y);
            Fontbatch.end();
        }
    }
    

    public BitmapFont getFont() {
        return font;
    }
}
