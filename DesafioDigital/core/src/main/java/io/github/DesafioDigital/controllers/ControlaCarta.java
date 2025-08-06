package io.github.DesafioDigital.controllers;

import io.github.DesafioDigital.utils.Inicializadores;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.DesafioDigital.utils.StringUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.DesafioDigital.models.Carta;
import io.github.DesafioDigital.utils.Draws;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

/** Classe responsável por controlar uma Carta. */
public class ControlaCarta {
    private Rectangle bounds;           // Representa os tamanhos e posições da carta.

    private Texture backTexture;        // Parte de trás (exterior) da carta.
    private Texture frontTexture;       // Parte da frente (interior) da carta.
    private Texture dispositivoTexture; // Imagem do dispositivo na carta.
    private Carta carta;

    private boolean cor = false;        // Indica se a cor de borda está ativada.
    private boolean fixa = false;       // Indica se a carta está fixada.
    private boolean clicked = false;    // Indica se a carta foi clicada.
    private boolean isFlipped= false;   // Indica se está mostrando a frente.
    private boolean isShaking = false;  // Indica se a carta está tremendo.
    private boolean isFlipping = false; // Indica se está no meio da animação.

    private float scaleX = 1.0f;        // Escala atual no eixo X.
    private float flipTime = 0.0f;      // Tempo atual da animação de flip.
    private float shakeTime = 0.0f;     // Tempo atual do tremor.
    private float flipDuration = 0.5f;  // Duração total da animação de flip.
    private float moveAmount = 1.095f;  // Movimento da carta.
    private float shakeAmount = 20.0f;  // Intensidade do tremor.
    private float shakeDuration = 0.5f; // Duração do tremor.

    private Color borderColor = null;   // Cor da borda da carta.

    // Atributos originais para restauração de posição e dimensão.
    private float originalX, originalY, originalWidth, originalHeight;

    public ControlaCarta(Carta carta, Rectangle retangulo, Texture back, Texture front) {
        this.carta = carta;
        this.bounds = retangulo;
        this.backTexture = back;
        this.frontTexture = front;
        this.dispositivoTexture = Inicializadores.inicializaTexture(carta.getImagemDispositivo());
        
        // Inicializa a posição original
        this.originalX = retangulo.x;
        this.originalY = retangulo.y;
        this.originalWidth = retangulo.width;
        this.originalHeight = retangulo.height;
    }
    
    public void dispose() {
        this.backTexture.dispose();
        this.frontTexture.dispose();
        this.dispositivoTexture.dispose();
    }

    public void movementCard() {
        if (!clicked) {
            ajustarDimensoes();
            clicked = true;
        } 
        else {
            restauraDimensoes();
            clicked = false;
        }
    }

    public boolean flip() {
        if (isFlipping || fixa) return false;
        isFlipping = true;
        flipTime = 0;
        return true;
    }

    public void shake() {
        isShaking = true;
        shakeTime = 0f;
    }

    public void update(float delta) {
        atualizarFlip(delta);
        atualizarShake(delta);
    }

    private void atualizarFlip(float delta) {
        if (!isFlipping) return;

        flipTime += delta;
        float halfDuration = flipDuration / 2;
        float delayTime = 0.05f;

        if (flipTime < halfDuration) {
            scaleX = Interpolation.sine.apply(1, 0.0000001f, flipTime / halfDuration);
        } 
        else if (flipTime >= halfDuration + delayTime && !isFlipped) {
            isFlipped = !isFlipped;
        }

        if (flipTime >= halfDuration + delayTime) {
            float openTime = flipTime - (halfDuration + delayTime);
            scaleX = Interpolation.sine.apply(0.0000001f, 1, openTime / halfDuration);
        }

        if (flipTime >= flipDuration + delayTime) {
            isFlipping = false;
        }
    }

    private void atualizarShake(float delta) {
        if (!isShaking) return;

        shakeTime += delta;
        float shakeOffset = (float) Math.sin(shakeTime * 20) * shakeAmount;
        bounds.setPosition(originalX + shakeOffset, originalY);

        if (shakeTime > shakeDuration) {
            isShaking = false;
            bounds.setPosition(originalX, originalY);
        }
    }

    public void draw(SpriteBatch batch, Viewport viewport, ControlaFonte fonte, ShapeRenderer shapeRenderer) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        if (isFlipped) drawFrontCarta(batch, viewport, fonte);
        else drawBackCarta(batch, viewport);

        // Se houver uma borda definida, desenha a borda ao redor da carta
        if (cor) drawColor(viewport, shapeRenderer, borderColor);
        if (clicked) drawColor(viewport, shapeRenderer, Color.BLACK);
    }

    public void drawFrontCarta(SpriteBatch batch, Viewport viewport, ControlaFonte fonte) {
        Texture texture = frontTexture;

        float cardHeight =  bounds.height * scaleX;
        float cardWidth = bounds.width * scaleX;

        float dispositivoWidth = cardWidth * 0.6f;
        float dispositivoHeight = cardHeight * 0.6f;
        float dispositivoX = bounds.x + (bounds.width - dispositivoWidth) / 2;
        float dispositivoY = bounds.y + bounds.height - dispositivoHeight - 10;

        String nome = StringUtils.addQuebraLinhaComLimiteCentralizado(
            carta.getNome(), 12
        );

        float fonteWidth = cardWidth * 0.8f;
        float fonteHeight = cardHeight * 0.4f;
        float fonteX = bounds.x + (bounds.width - fonteWidth) / 2;
        float fonteY = bounds.y + (bounds.height - fonteHeight) / 2 - 100;

        batch.begin();
            batch.draw(texture, bounds.x + bounds.width * (1 - scaleX) / 2, bounds.y, bounds.width * scaleX, bounds.height);
            Draws.desenhaTextureProporcional(batch, dispositivoTexture, dispositivoX, dispositivoY, dispositivoWidth, dispositivoHeight);
        batch.end();
        Draws.desenhaTextoProporcional(fonte, nome, Color.BLACK, fonteX, fonteY, fonteWidth, fonteHeight, Math.min (1.0f, 1.2f * 26 / nome.length()));
    }

    public void drawBackCarta(SpriteBatch batch, Viewport viewport) {
        Texture texture = backTexture;
        float dispositivoX, dispositivoY, dispositivoWidth, dispositivoHeight;

        dispositivoWidth = bounds.width * 0.9f * scaleX;
        dispositivoHeight = bounds.height * 0.9f * scaleX;
        dispositivoX = bounds.x + (bounds.width - dispositivoWidth) / 2;
        dispositivoY = bounds.y + (bounds.height - dispositivoHeight) / 2;

        batch.begin();
            batch.draw(texture, bounds.x + bounds.width * (1 - scaleX) / 2, bounds.y, bounds.width * scaleX, bounds.height);
            Draws.desenhaTextureProporcional(batch, dispositivoTexture, dispositivoX, dispositivoY, dispositivoWidth, dispositivoHeight);
            batch.end();
    }

    public void drawColor(Viewport viewport, ShapeRenderer shapeRenderer, Color cor) {
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setColor(cor);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            // Camada 1: borda mais grossa
            shapeRenderer.rect(bounds.x - 4, bounds.y - 4, bounds.width + 8, bounds.height + 8); // Aumenta mais a espessura
        
            // Camada 2: borda intermediária
            shapeRenderer.rect(bounds.x - 2, bounds.y - 2, bounds.width + 4, bounds.height + 4); // Borda intermediária
            
            // Camada 3: borda original
            shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height); // Desenha o retângulo original
        shapeRenderer.end();
    }

    public void ajustarDimensoes() {
        float newWidth = originalWidth * moveAmount;
        float newHeight = originalHeight * moveAmount;
        float newX = originalX - (newWidth - originalWidth) / 2;
        float newY = originalY - (newHeight - originalHeight) / 2;
        setDimensoes(newX, newY, newWidth, newHeight);
    }

    public void restauraDimensoes() {
        setDimensoes(originalX, originalY, originalWidth, originalHeight);
    }

    public void setDimensoes(float x, float y, float width, float height) {
        bounds.set(x, y, width, height);
    }

    public boolean isClicked() { 
        return clicked; 
    }

    public boolean isFixa() { 
        return fixa; 
    }

    public boolean isFlipped() { 
        return isFlipped; 
    }

    public boolean isFlipping() { 
        return isFlipping; 
    }
    
    public boolean isCor() {
        return cor;
    }

    public Rectangle getBounds() { 
        return bounds; 
    }

    public Texture getBackTexture() { 
        return backTexture; 
    }

    public Texture getFrontTexture() { 
        return frontTexture; 
    }

    public Texture getDispositivoTexture() { 
        return dispositivoTexture; 
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }


    public void setCor(boolean cor) {
        this.cor = cor;
    }

    public void setClicked(boolean clicked) { 
        this.clicked = clicked; 
    }

    public void setFixa(boolean fixa) { 
        this.fixa = fixa; 
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
    }

    public void setFlipped(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }
}
