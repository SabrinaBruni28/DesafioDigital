package com.badlogic.desafiodigital.controllers;

import com.badlogic.desafiodigital.utils.Inicializadores;
import com.badlogic.desafiodigital.utils.Arquivos;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.desafiodigital.models.Carta;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

/** Classe responsável por controlar uma lista de Cartas. */
public class ControlaCartas {
    // Lista de cartas.
    private ArrayList<ControlaCarta> cards;
    private boolean nonFixedCardClicked = false;

    public ControlaCartas(
            ArrayList<Carta> cartas,
            int rows, int cols, 
            float cardWidth, float cardHeight, 
            float marginX, float marginY, 
            float spacingX, float spacingY,
            Texture backTexture, Texture frontTexture) {

        // Inicialização da lista de cartas. 
        cards = new ArrayList<>();

        // Embaralha as cartas.
        
        int i = 0;
        // Inicializa as cartas e define posições.
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                float x = marginX + col * (cardWidth + spacingX);
                float y = marginY + row * (cardHeight + spacingY);
                cards.add(Inicializadores.inicializaControlaCarta(
                    cartas.get(i), x, y, cardWidth, cardHeight, backTexture, frontTexture));
                i++;
            }
        }
    }

    public void dispose() {
        for (ControlaCarta card : cards) card.dispose();
    }

    public void draw(SpriteBatch batch, Viewport viewport, ControlaFonte fonte, ShapeRenderer shapeRenderer) {
        for (ControlaCarta card : cards) {
            card.update(Gdx.graphics.getDeltaTime());
            card.draw(batch, viewport, fonte, shapeRenderer);
        }
    }

    public void nonFixedCard(ControlaCarta card) {
        if (card.isClicked()) nonFixedCardClicked = false;
        else nonFixedCardClicked = true;
    }

    public boolean touchedCarta(float x, float y, Sound clickSound) {
        // Se uma carta não fixa foi clicada, não permita a interação com cartas fixas.
        if (nonFixedCardClicked) {
            for (ControlaCarta card : cards) {
                // Permite o clique apenas nas cartas não fixas.
                if (card.getBounds().contains(x, y) && !card.isFixa()) {
                    nonFixedCard(card);
                    card.movementCard();
                    clickSound.play();
                    return true;
                }
            }
            return false;
        }
    
        // Caso contrário, permite o clique em todas as cartas.
        for (ControlaCarta card : cards) {
            if (card.getBounds().contains(x, y)) {
                if (!card.isFixa()) nonFixedCard(card);
                card.movementCard();
                clickSound.play();
                return true;
            }
        }
        return false;
    }

    public ArrayList<ControlaCarta> cartasClicked(Sound acertoSound, Sound erroSound) {
        ArrayList<ControlaCarta> clickedCards = getClickedCards();
    
        if (clickedCards != null) {
            if (clickedCards.size() == 2) {
                // Valta a permitir cartas fixas.
                nonFixedCardClicked = false;

                // Pega as cartas clicadas.
                ControlaCarta firstCard = clickedCards.get(0);
                ControlaCarta secondCard = clickedCards.get(1);
    
                // Verifica se as cartas combinam
                if (firstCard.getCarta().getId() == secondCard.getCarta().getId()) {
                    processMatchingCards(firstCard, secondCard, acertoSound);
                    defineCor(clickedCards);
                } 
                else {
                    processNonMatchingCards(firstCard, secondCard, erroSound);
                    return new ArrayList<ControlaCarta>();
                }
            }
        }
        return clickedCards;
    }

    public boolean cartaClicked(int idResposta, Sound acertoSound, Sound erroSound){
        ControlaCarta clickedCard = getClickedCard();
        
        if (clickedCard != null ) {
            // Verifica se a carta é a correta.
            if (clickedCard.getCarta().getId() == idResposta) {
                processCartaCorreta(clickedCard, acertoSound);
                return true;
            } 
            // Se aa carta é errada.
            else {
                processCartaErrada(clickedCard, erroSound);
                return false;
            }
        }
        return false;
    }

    public void defineCor(ArrayList<ControlaCarta> cards) {
        // Define uma cor de borda para as cartas corretas
        for (ControlaCarta carta: cards)
            if (carta.getBorderColor() != null) carta.setCor(true);
    }

    private void processCartaCorreta(
            ControlaCarta card, Sound acertoSound) {

        if (acertoSound != null) acertoSound.play(1.0f);
    }

    private void processCartaErrada(
            ControlaCarta card, Sound erroSound) {

        if (erroSound != null) erroSound.play(1.0f);
        card.shake();
    }
    
    private void processMatchingCards(
            ControlaCarta firstCard, ControlaCarta secondCard, Sound acertoSound) {

        if (acertoSound != null) acertoSound.play(1.0f);
        firstCard.flip();
        secondCard.flip();
        firstCard.setFixa(true);
        secondCard.setFixa(true);
        firstCard.movementCard();
        secondCard.movementCard();
    }

    private void processNonMatchingCards(
            ControlaCarta firstCard, ControlaCarta secondCard, Sound erroSound) {

        if (erroSound != null) erroSound.play(1.0f);
        firstCard.shake();
        secondCard.shake();
        firstCard.movementCard();
        secondCard.movementCard();
    }

    public void restauraCartas(ArrayList<ControlaCarta> cartas) {
        for (ControlaCarta carta : cartas) {
            carta.restauraDimensoes();
            carta.setClicked(false);
        }
    }

    public boolean isWin() {
        for (ControlaCarta card : cards) {
            if (!card.isFixa()) return false;
        }
        return true;
    }

    public ArrayList<ControlaCarta> getCards() {
        return cards;
    }

    public ArrayList<ControlaCarta> getClickedCards() {
        ArrayList<ControlaCarta> clickedCards = new ArrayList<>();
        ArrayList<ControlaCarta> fixedClickedCards = new ArrayList<>();
    
        for (ControlaCarta card : cards) {
            if (card.isClicked()) {
                if (card.isFixa()) fixedClickedCards.add(card); // Armazena cartas fixas que estão clicadas
                else clickedCards.add(card); // Armazena cartas não fixas que estão clicadas
            }
        }
    
        // Retorna as duas cartas clicadas se houver, caso contrário, retorna uma carta fixa clicada se houver
        if (clickedCards.size() == 2) return clickedCards;
        else if (fixedClickedCards.size() == 1) return fixedClickedCards;
        else return null; // Nenhuma carta válida foi clicada
    }    

    public ControlaCarta getClickedCard() {
        for (ControlaCarta card : cards) {
            // Analisa as que estão clicadas.
            if (card.isClicked()) {
                return card;
            }
        }
        return null; 
    }

    public void setBolderColors() {
        ArrayList<Color> cores = Arquivos.readCoresFromFile("gameConfig/cores.json");
        for (ControlaCarta carta: cards) carta.setBorderColor(cores.get(carta.getCarta().getId() - 1));
    }

    public void setCartasFlipped(boolean isFlipped) {
        for (ControlaCarta carta : cards) carta.setFlipped(isFlipped);
    }
}
