package com.badlogic.memoria;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Fonte {
    /* Cenário para os textos. */
    public SpriteBatch Fontbatch;
    /* Fonte para os textos. */
    public BitmapFont font;

    public Fonte(float tamanho) {
        /* Inicializa o SpriteBatch. */
        Fontbatch = new SpriteBatch();
        
        /* Carrega a fonte a partir do arquivo .fnt. */
        font = new BitmapFont(Gdx.files.internal("Fonte/default.fnt"), Gdx.files.internal("Fonte/default.png"), false);
        
        /* Aumentar o tamanho da fonte com escala. */
        font.getData().setScale(tamanho);
        /* Alterando a cor da fonte. */
        font.setColor(Color.BLACK);
    }

    public void draw(String string, float x, float y) {
        // Inicia o desenho
        Fontbatch.begin();
        /* Desenha os textos. */
        font.draw(Fontbatch, string, x, y);
        // Termina o desenho
        Fontbatch.end();
    }

    public void dispose() {
        this.Fontbatch.dispose();
        this.font.dispose();
    }
}
