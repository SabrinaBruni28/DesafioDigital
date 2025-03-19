package com.badlogic.memoria;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;

public class Card {
    public Rectangle bounds;
    
    private Texture backTexture;
    private Texture frontTexture;

    private boolean isFlipped; // Indica se está mostrando a frente
    private boolean isFlipping; // Se está no meio da animação
    private boolean fixa; // Indica se a carta está fixada

    private float flipTime; // Tempo atual da animação
    private float flipDuration; // Duração total da animação
    private float scaleX; // Escala atual no eixo X

    public Card(float x, float y, float width, float height, Texture back, Texture front) {
        this.bounds = new Rectangle(x, y, width, height);
        this.backTexture = back;
        this.frontTexture = front;
        this.isFlipped = false;
        this.isFlipping = false;
        this.fixa = false;
        this.flipDuration = 0.5f;
        this.scaleX = 1f;
    }

    public boolean flip() {
        // Se a carta já estiver no meio da animação, não faz nada
        if (isFlipping) return false;
        
        if (!fixa){
            // Inicia a animação de flip
            isFlipping = true;
            flipTime = 0;
            
            // Alterna o estado da carta entre virada ou não
            isFlipped = !isFlipped;
            return true;
        }
        return false;
    }

    public void update(float delta) {
        if (this.isFlipping) {
            flipTime += delta;
            
            // Interpolação para a escala no eixo X (simulando a rotação)
            if (flipTime < flipDuration / 2) {
                scaleX = Interpolation.sine.apply(1, 0, flipTime / (flipDuration / 2));
            }
            else {
                scaleX = Interpolation.sine.apply(0, 1, (flipTime - flipDuration / 2) / (flipDuration / 2));
            }
            
            if (flipTime >= flipDuration) {
                isFlipping = false;
            }
        }
    }
    
    public void draw(SpriteBatch batch) {
        Texture texture = isFlipped ? frontTexture : backTexture;
        batch.draw(texture, bounds.x + bounds.width * (1 - scaleX) / 2, bounds.y, bounds.width * this.scaleX, this.bounds.height);
    }
    
    public void dispose() {
        this.backTexture.dispose();
        this.frontTexture.dispose();
    }
    
    public boolean isFixa() {
        return this.fixa;
    }

    public void setFixa(boolean fixa) {
        this.fixa = fixa;
    }

    public Rectangle getBounds() {
        return this.bounds;
    }

    public Texture getBackTexture() {
        return this.backTexture;
    }

    public Texture getFrontTexture() {
        return this.frontTexture;
    }

    public boolean isFlipped() {
        return this.isFlipped;
    }

    public boolean isFlipping() {
        return this.isFlipping;
    }

    public float getFlipTime() {
        return this.flipTime;
    }

    public void setFlipTime(float flipTime) {
        this.flipTime = flipTime;
    }

    public float getFlipDuration() {
        return this.flipDuration;
    }

    public void setFlipDuration(float flipDuration) {
        this.flipDuration = flipDuration;
    }

    public float getScaleX() {
        return this.scaleX;
    }
}
