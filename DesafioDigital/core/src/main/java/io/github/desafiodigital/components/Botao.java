package io.github.desafiodigital.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.desafiodigital.utils.Draws;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/** Classe para representar um botão na tela. */
public class Botao {
    // Botão imagem.
    private Texture botaoTexture;
    // Botão imagem para mudar.
    private Texture outroTexture;

    // Botão molde.
    private Rectangle retangulo;

    // Atributos originais para restauração.
    private float originalX, originalY, originalWidth, originalHeight;

    public Botao (Texture botaoTexture, Rectangle retangulo) {
        this.botaoTexture = botaoTexture;
        this.retangulo = retangulo;

        this.originalX = retangulo.x;
        this.originalY = retangulo.y;
        this.originalWidth = retangulo.width;
        this.originalHeight = retangulo.height;
    }

    public void dispose() {
        // Exclui os recursos do botão.
        botaoTexture.dispose();
    }

    public void drawBotaoTexture(SpriteBatch batch) {
        // Desenha o botão.
        Draws.desenhaTextureProporcional(batch, botaoTexture, retangulo.x, retangulo.y, retangulo.width, retangulo.height);
    }

    public void drawOutroTexture(SpriteBatch batch) {
        // Desenha o botão.
        Draws.desenhaTextureProporcional(batch, outroTexture, retangulo.x, retangulo.y, retangulo.width, retangulo.height);
    }
    
    public boolean touched(float x, float y) {
        // Verifica se o botão foi clicado.
        return (retangulo.contains(x, y));
    }

    public void restauraDimensoes() {
        setDimensoes(originalX, originalY, originalWidth, originalHeight);
    }

    public Rectangle getForma() {
        return retangulo;
    }

    public float getOriginalHeight() {
        return originalHeight;
    }

    public float getOriginalWidth() {
        return originalWidth;
    }

    public float getOriginalY() {
        return originalY;
    }

    public float getOriginalX() {
        return originalX;
    }

    public Texture getOutroTexture() {
        return outroTexture;
    }

    public void setOutroTexture(Texture outroTexture) {
        this.outroTexture = outroTexture;
    }

    public void setBotaoTexture(Texture botaoTexture) {
        this.botaoTexture = botaoTexture;
    }

    public void setDimensoes(float x, float y, float width, float height) {
        retangulo.set(x, y, width, height);
    }
}

