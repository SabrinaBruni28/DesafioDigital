package io.github.DesafioDigital.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

/** Classe para controlar os arquivos de imagem. */
public class ControlaTexture {
    
    // Imagem.
    private Texture elementoTexture;
    // Para dar movimento ao texture se desejar.
    private Sprite sprite;
    // Para mudar a posição do texture para o movimento.
    private float x, y;
    // Direção inicial por padrão direita/para cima.
    private boolean direcionamento;

    public ControlaTexture(Texture elementoTexture, Sprite sprite) {
        this.elementoTexture = elementoTexture;
        this.sprite = sprite;
        this.x = sprite.getX();
        this.y = sprite.getY();
        this.direcionamento = true;
    }

    public void dispose() {
        this.elementoTexture.dispose();
        sprite.getTexture().dispose();
    }

    public void draw(SpriteBatch batch) {
        batch.begin();
            sprite.draw(batch);
        batch.end();
    }

    public void movimentX(float velocidadeX, float limiteInfX, float limiteSupX) {
        // Verifique se atingiu o limite superior ou inferior para inverter a direção
        if (x >= limiteSupX) {
            direcionamento = false; // Mude para descer
        } 
        else if (x <= limiteInfX) {
            direcionamento = true; // Mude para subir
        }

        // Atualize a posição com base na direção
        if (direcionamento) {
            x += velocidadeX * Gdx.graphics.getDeltaTime();
        } 
        else {
            x -= velocidadeX * Gdx.graphics.getDeltaTime();
        }

        // Aplique a posição atualizada ao sprite
        sprite.setX(x);
    }

    public void movimentY(float velocidadeY, float limiteInfY, float limiteSupY) {
        // Verifique se atingiu o limite superior ou inferior para inverter a direção
        if (y >= limiteSupY) {
            direcionamento = false; // Mude para descer
        } 
        else if (y <= limiteInfY) {
            direcionamento = true; // Mude para subir
        }

        // Atualize a posição com base na direção
        if (direcionamento) {
            y += velocidadeY * Gdx.graphics.getDeltaTime();
        } 
        else {
            y -= velocidadeY * Gdx.graphics.getDeltaTime();
        }

        // Aplique a posição atualizada ao sprite
        sprite.setY(y);
    }

    public Texture getElementoTexture() {
        return elementoTexture;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
