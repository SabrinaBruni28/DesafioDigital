package io.github.DesafioDigital.controllers;

import io.github.DesafioDigital.components.Botao;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Sound;

/** Classe abstrata para controlar qualquer formato de botão. */
public class ControlaBotao {
    private Botao botao;
    private boolean somAtivo;
    private boolean ativo, whithBotao; // Indica se o botão está ativo (clicável)

    public ControlaBotao(Botao botao) {
        this.botao = botao;
        this.ativo = true; // Botão começa ativo
        this.somAtivo = false; // som começa desativado.
        this.whithBotao = true; // começa com botão original.
    }

    public void dispose() {
        this.botao.dispose();
    }

    public void draw(SpriteBatch batch) {
        batch.begin();
    
        if (!ativo) {
            // Se o botão estiver desativado, aplica uma cor semitransparente (opaca) somente no botão.
            batch.setColor(1f, 1f, 1f, 0.5f); // Cor normal, mas com 50% de opacidade (transparente)
        } 
        else {
            // Caso contrário, mantém a cor original.
            batch.setColor(1f, 1f, 1f, 1f); // Cor normal (sem transparência)
        }

        // Desenha o botão com a cor ajustada
        if (whithBotao) botao.drawBotaoTexture(batch);
        else botao.drawOutroTexture(batch);
        
        // Restaura a cor do batch para o padrão após o desenho do botão.
        batch.setColor(1f, 1f, 1f, 1f);

        batch.end();
    }

    /** Método responsável por conferir que as coordenadas estão dentro da área do botão. */
    public boolean touchedBotao(float x, float y, Sound click) {
        if (!ativo) return false; // Ignora interação se o botão estiver desativado.

        // Confere se o botão foi clicado.
        if (botao.touched(x, y)) {
            if (click != null) click.play(1.0f);
            return true;
        }
        return false;
    }

    /** Método responsável por movimentar o botão, aumentando seu tamanho
     *  de acordo com a quantidade de movimento do parâmetro quando o mouse está sobre o botão. */
    public void movementBotao(float x, float y, float moveAmount, Sound hover) {
        if (!ativo) return; // Não permite movimento se o botão estiver desativado.

        // Movimenta o botão ao passar o mouse.
        if (botao.touched(x, y)) {
            if (!somAtivo) {
                if (hover != null) hover.play(1.0f);
                somAtivo = true;
            }
            ajustarDimensoesBotao(moveAmount);
        } 
        else {
            botao.restauraDimensoes();
            somAtivo = false;
        }
    }

    /** Método responsável por mudar a imagem do botão a ser exibida 
     * para o outro texture quando o mouse está sobre o botão. */
    public void mudaBotao(float x, float y, float moveAmount) {
        if (!ativo) return; // Não permite a mudança se o botão estiver desativado.
        if (botao.getOutroTexture() == null) return;

        // Muda o desenho do botão ao passar o mouse.
        if (botao.touched(x, y)) whithBotao = false;
        else whithBotao = true;
    }

    /** Método responsável por mudar a imagem do botão a ser exibida 
     * para o outro texture quando o botão é clicado. */
    public void mudaBotao() {
        if (!ativo) return; // Não permite a mudança se o botão estiver desativado.
        if (botao.getOutroTexture() == null) return;
        whithBotao = !whithBotao;
    }

    public void ajustarDimensoesBotao(float moveAmount) {
        // x, y, width, height
        float newWidth = botao.getOriginalWidth() * moveAmount;
        float newHeight = botao.getOriginalHeight() * moveAmount;
        float newX = botao.getOriginalX() - (newWidth - botao.getOriginalWidth()) / 2;
        float newY = botao.getOriginalY() - (newHeight - botao.getOriginalHeight()) / 2;
        botao.setDimensoes(newX, newY, newWidth, newHeight);
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public boolean isWhithBotao() {
        return whithBotao;
    }

    public Botao getBotao() {
        return botao;
    }

    public void setAtivo(boolean ativo) {
        botao.restauraDimensoes();
        this.ativo = ativo;
    }
}

