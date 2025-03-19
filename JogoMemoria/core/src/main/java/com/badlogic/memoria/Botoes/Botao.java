package com.badlogic.memoria.Botoes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Botao {
    /* Botão imagem*/
    private Texture botaoTexture;
    /* Botão molde. */
    private Rectangle botaoRetangulo;
    /* Sons do jogo. */
    private Sound clickSound;
    /* Tamanho original do botão */
    private float originalWidth, originalHeight;
    /* Posição original do botão */
    private float originalX, originalY;
    /* Variável de movimento */
    private float moveAmount; // Quantidade de movimento
    
    public Botao(String imagemBotao, String somClick, float x, float y, float width, float height) {
        /* Inicialização da imagem. */
        botaoTexture = new Texture(imagemBotao);
        /* Inicialização do botão */
        botaoRetangulo = new Rectangle(x, y, width, height);
        /* Inicialização do som. */
        clickSound = Gdx.audio.newSound(Gdx.files.internal(somClick));
        /* Movimentação do botão */
        moveAmount = 1.2f;
        /* Guarda o tamanho original */
        originalWidth = width;
        originalHeight = height;
        /* Guarda a posição original */
        originalX = x;
        originalY = y;
    }
    
    public void draw(SpriteBatch batch) {
        /* Desenha o botão.. */
        batch.draw(botaoTexture, botaoRetangulo.x, botaoRetangulo.y, botaoRetangulo.width, botaoRetangulo.height);
    }
    
    public boolean touched(float x, float y){
        /* Verifica se o botão foi clicado. */
        if (botaoRetangulo.contains(x, y)) {
            clickSound.play();
            return true;
        }
        return false;
    }
    
    public void movement(float x, float y) {
        /* Verifica se o mouse está sobre o botão */
        if (botaoRetangulo.contains(x, y)) {
            // Aumenta o botão igualmente em todos os lados enquanto o mouse está sobre ele
            float newWidth = originalWidth *moveAmount;  // Aumenta 20%
            float newHeight = originalHeight * moveAmount;
            
            // Reposiciona o botão para mantê-lo centrado
            botaoRetangulo.x = originalX - (newWidth - originalWidth) / 2;
            botaoRetangulo.y = originalY - (newHeight - originalHeight) / 2;
            
            // Define as novas dimensões
            botaoRetangulo.width = newWidth;
            botaoRetangulo.height = newHeight;
        } 
        
        else {
            // Retorna ao tamanho e à posição originais quando o mouse sai
            botaoRetangulo.x = originalX;
            botaoRetangulo.y = originalY;
            botaoRetangulo.width = originalWidth;
            botaoRetangulo.height = originalHeight;
        }
    }
    
    public void dispose() {
        this.botaoTexture.dispose();
        this.clickSound.dispose();
    }
    
    public void setMoveAmount(float moveAmount) {
        this.moveAmount = moveAmount;
    }

    public Rectangle getBotaoRetangulo() {
        return botaoRetangulo;
    }
}
