package com.badlogic.memoria.Telas;

import java.util.ArrayList;
import java.util.Collections;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.memoria.Card;
import com.badlogic.memoria.MemoriaGame;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {

    final MemoriaGame game;

    /* Sons do jogo. */
    private Sound clickSound;
    private Sound erroSound;
    private Sound acertoSound;

    /* Imagens do cenário. */
    private Texture cardBack;

    /* Lista das imagens frutas. */
    private ArrayList<Texture>frutasTextures;
    
    /* Para cálculo de coordenadas. */
    private Vector2 touchPos;

    /* Lista de cartas. */
    private Array<Card> cards;

    /* Lista das imagens. */
    private ArrayList<String> imagens;

    // Configurações da grade
    private int rows; // Linhas
    private int cols; // Colunas
    private float cardWidth;
    private float cardHeight;
    private float spacingY; // Espaçamento entre as cartas horizontalmente
    private float spacingX; //Espaçamento entre as cartas verticalmente

    private long tempoInicial;
    private long tempoDecorrido;
    private float segundosDecorridos;

    private float tempoLimite;

    public GameScreen(final MemoriaGame game) {
        this.game = game;

        // Armazena o tempo inicial
        tempoInicial = TimeUtils.millis();

        rows = 2 + this.game.getNivel();
        cols = 3 + this.game.getNivel();
        
        tempoLimite = 30.0f * this.game.getNivel();

        /* Definindo o tamanho da carta. */
        cardWidth = this.game.getWorldHeight() / (cols + this.game.getNivel()); // Calcula a largura da carta
        cardHeight = cardWidth; // Cartas quadradas

        spacingY = (this.game.getWorldHeight() - rows*cardHeight)/(rows + 1);
        spacingX = (this.game.getWorldWidth() - cols*cardWidth)/(cols + 1);

        /* Inicialização dos sons e música. */
        acertoSound = Gdx.audio.newSound(Gdx.files.internal("Sons_Musics/acerto.mp3"));
        erroSound = Gdx.audio.newSound(Gdx.files.internal("Sons_Musics/error.mp3"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("Sons_Musics/click.mp3"));

        /* Inicialização da imagem da carta. */
        cardBack = new Texture("Cartas/card_back.png");

        // Inicializar e carregar as diferentes texturas frontais
        imagens = new ArrayList<>();
        imagens.add("Frutas/banana.jpg");
        imagens.add("Frutas/cereja.jpg");
        imagens.add("Frutas/goiaba.jpg");
        imagens.add("Frutas/kiwi.jpg");
        imagens.add("Frutas/limao.jpg");
        imagens.add("Frutas/mirtilo.jpg");
        imagens.add("Frutas/uva.jpg");
        imagens.add("Frutas/abacaxi.jpg");
        imagens.add("Frutas/laranja.jpeg");
        imagens.add("Frutas/melancia.png");
        imagens.add("Frutas/abacate.png");
        imagens.add("Frutas/amora.jpg");
        imagens.add("Frutas/framboesa.png");
        imagens.add("Frutas/guarana.jpg");
        imagens.add("Frutas/mamao.png");
        imagens.add("Frutas/manga.png");
        imagens.add("Frutas/morango.jpg");
        imagens.add("Frutas/pera.png");

        // Inicializar e carregar as diferentes texturas frontais
        frutasTextures = new ArrayList<>();
    
        /* Adicionando a quantidade de frutas necessárias. */
        for (int i = 0; i < (rows*cols)/2; i++) {
            int item = i % imagens.size();
            frutasTextures.add(new Texture(imagens.get(item)));
        }
        
        /* Duplicando a lista. */
        frutasTextures.addAll(frutasTextures);

        /* Embaralha as imagens de frente. */
        Collections.shuffle(frutasTextures);

        /* Inicialização da lista de cartas. */
        cards = new Array<>();

        /* Inicialização do vetor de coordenadas. */
        touchPos = new Vector2();

        /* Cria as cartas e as posiciona. */
        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Calcula a posição x e y da carta
                float x = col * (cardWidth + 1) + spacingX + 0.8f;
                float y = row * (cardHeight + 1) + spacingY + 0.2f/this.game.getNivel();
                
                // Atribui uma textura de frente diferente para cada carta
                Texture frontTexture = frutasTextures.get(index);
                
                // Adiciona a carta à lista
                cards.add(new Card(x, y, cardWidth, cardHeight, cardBack, frontTexture));
                
                index++; // Avança para a próxima textura
            }
        }
    }

    @Override
    public String toString() {
        return "GameScreen"; // Identificador da tela
    }

    @Override
    public void show() {
        // Prepare your screen here.
    }

    @Override
    public void render(float delta) {
        /* Desenha na tela. */
        draw();
        /* Detecta entradas do usuário. */
		input();
        /* Executa a lógica do programa. */
        logic();
    }

    private void input() {
       /* Faz alguma coisa quando clica ou toca a tela. */
       if (Gdx.input.isTouched()) { // If the user has clicked or tapped the screen
            /* Pega onde o toque aconteceu na tela. */
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); 
            /* Converte as unidades da janela para as coordenadas do cenário. */
            this.game.viewport.unproject(touchPos);

           /* Verifica se uma carta foi clicada. */
           for (Card card : cards) {
                if (card.bounds.contains(touchPos.x, touchPos.y) && !card.isFlipping()) {
                    /* Gira a carta. */
                    if (card.flip()) clickSound.play(); 
                }
            }
        }
    }

    private void logic() {
        /* Confere se o jogo terminou. */
        isWinOrOver();

        // Calcula o tempo decorrido em milissegundos
        tempoDecorrido = TimeUtils.millis() - tempoInicial;

        // Converte para segundos
        segundosDecorridos = tempoDecorrido / 1000f;

        Array<Card> flippedCards = getFlippedCards();
        
        if (flippedCards != null) {
            /* Pega as cartas viradas. */
            Card firstCard = flippedCards.get(0);
            Card secondCard = flippedCards.get(1);

            /* Verifica se as cartas combinam. */
            if (firstCard.getFrontTexture() == secondCard.getFrontTexture()) {
                acertoSound.play(0.8f);
                /* Se as cartas combinam elas são fixadas. */
                firstCard.setFixa(true);
                secondCard.setFixa(true);
            } 
            else {
                erroSound.play(0.8f);
                /* Se as cartas não combinam, elas são viradas de volta. */
                firstCard.flip();
                secondCard.flip();
            }
        }  
    }

    private void draw() {
        /* Limpa a tela. */
        ScreenUtils.clear(Color.WHITE);
        this.game.viewport.apply();
    
        // Define a matriz de projeção do SpriteBatch
        this.game.spritebatch.setProjectionMatrix(this.game.viewport.getCamera().combined);

        // Inicia o desenho
        this.game.spritebatch.begin(); // Use o spriteBatch, não game.batch
            
            /* Desenha o background. */
            this.game.spritebatch.draw(this.game.backgroundTexture, 0, 0, this.game.getWorldWidth(), this.game.getWorldHeight());
           
            // Desenha as cartas e atualiza a animação de virar
            for (Card card : cards) {
                card.update(Gdx.graphics.getDeltaTime());
                card.draw(this.game.spritebatch);
               
            }

        // Termina o desenho
        this.game.spritebatch.end();


        this.game.fonte.draw("Nível " + this.game.getNivel(), 350, 585);
        this.game.fonte.draw("Tempo: " + (int) segundosDecorridos, 80, 585);
    }

    public Array<Card> getFlippedCards() {
        Array<Card> flippedCards = new Array<>();

        /* Pega as cartas que estão viradas para frente. */
        for (Card card : cards) {
            /* Analisa as que não estão girando e nem fixa. */
            if (card.isFlipped() && !card.isFlipping() && !card.isFixa()) {
                flippedCards.add(card);
            }
        }

        // Retorna as duas cartas viradas.
        if (flippedCards.size == 2) {
            return flippedCards;
        } 
        else {
            return null; // Retorna null se não houver duas cartas viradas
        }
    }

    public void isWinOrOver() {
        int count = 0;
        /* Confere se todas as cartas estão fixas. */
        for (Card card : cards) {
            if (!card.isFixa()) {
                count++;
            }
        }
        /* Se todas as cartas estiverem fixas o jogo terminou e o usuário ganhou. */
        if (count == 0) {
            this.game.getPartida().setTempo(this.game.getNivel()-1, segundosDecorridos);
            /* Muda para a próxima tela. */
			this.game.setScreen(new WinScreen(this.game));
			dispose();
        }

        /* Se o tempo limite for excedido. */
        if (segundosDecorridos >= tempoLimite){
            this.game.getPartida().setTempo(this.game.getNivel()-1, segundosDecorridos);
            /* Muda para a próxima tela. */
			this.game.setScreen(new OverScreen(this.game));
			dispose();
        }
        /* Caso contrário o jogo continua. */
        return;
    }

    @Override
    public void resize(int width, int height) {
        this.game.viewport.update(width, height, true); // true centers the camera
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        /* Exclui as variáveis quando a tela é fechada. */
        for (Texture frutaTexture : this.frutasTextures) frutaTexture.dispose();

        for (Card card : this.cards) card.dispose();

        this.cardBack.dispose();
        this.clickSound.dispose();
        this.erroSound.dispose();
        this.acertoSound.dispose();
    }
}