package io.github.desafiodigital.controllers;

import io.github.desafiodigital.utils.Inicializadores;
import io.github.desafiodigital.utils.StringUtils;
import io.github.desafiodigital.models.Cenario;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.desafiodigital.utils.Draws;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

/** Classe que representa um cenário. */
public class ControlaCenario {
    private Rectangle bounds;           // Representa os tamanhos e posições da carta.

    private Texture cenarioTexture;     // Imagem do cenário.
    private Texture fundoTexture;       // Fundo do cenário.
    private Cenario cenario;

    public ControlaCenario(Cenario cenario, Rectangle retangulo, Texture fundo) {
        this.cenario = cenario;
        this.bounds = retangulo;
        this.fundoTexture = fundo;
        this.cenarioTexture = Inicializadores.inicializaTexture(cenario.getImagemCenario());
    }
    
    public void dispose() {
        this.fundoTexture.dispose();
        this.cenarioTexture.dispose();
    }

    public void setDimensoes(float x, float y, float width, float height) {
        bounds.set(x, y, width, height);
    }

    public void drawFundo(SpriteBatch batch) {
        batch.draw(fundoTexture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void drawCenario(SpriteBatch batch, float x, float y, float width, float height) {
        batch.draw(cenarioTexture, x, y, width, height);
    }

    public void draw(SpriteBatch batch, Viewport viewport, ControlaFonte fonte) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        float cenarioWidth = bounds.width * 0.9f;
        float cenarioHeight = bounds.height * 0.6f;
        float cenarioX = bounds.x + (bounds.width - cenarioWidth) / 2;
        float cenarioY = bounds.y + bounds.height - cenarioHeight - 10;

        String texto = StringUtils.addQuebraLinhaComLimite(
            StringUtils.justificarTexto(
                cenario.getPergunta(), 
                42
            ),
            42
        );
        
        float fonteWidth = bounds.width * 0.77f;
        float fonteHeight = bounds.height * 0.38f;
        float fonteX = bounds.x + 100;
        float fonteY = bounds.y;

        batch.begin();
            drawFundo(batch);
            Draws.desenhaTextureProporcional(batch, cenarioTexture, cenarioX, cenarioY, cenarioWidth, cenarioHeight);
        batch.end();
        Draws.desenhaTextoProporcional(fonte, texto, Color.BLACK, fonteX, fonteY, fonteWidth, fonteHeight, 1.2f);
    }   

    public Rectangle getBounds() { 
        return bounds; 
    }

    public Cenario getCenario() {
        return cenario;
    }

    public void setCenario(Cenario cenario) {
        this.cenario = cenario;
    }
}
