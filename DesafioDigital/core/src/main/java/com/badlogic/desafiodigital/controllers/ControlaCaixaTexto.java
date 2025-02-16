package com.badlogic.desafiodigital.controllers;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

/** Classe para controlar caixas de textos na tela. */
public class ControlaCaixaTexto extends Actor {
    private Stage stage;
    private Skin skin;
    private ArrayList<TextField> textFields;

    private int qntCaixa;

    // Texto escrito na caixa pelo usuário.
    private String[] textos;

    public ControlaCaixaTexto(int qntCaixa, Skin skin, Stage stage, Viewport view) {
        this.stage = stage;
        stage.setViewport(view);
        Gdx.input.setInputProcessor(stage); // Configura o input para o stage.

        this.skin = skin;
        this.qntCaixa = qntCaixa;

        // Inicializa o array de textos.
        textos = new String[qntCaixa];
        textFields = new ArrayList<>();

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void dispose() {
        this.skin.dispose();
        this.stage.dispose();
        for (TextField textField : textFields) textField.getStyle().font.dispose();
    }

    public void setupTextFields(String arquivoFnt, String arquivoPng, Sound tecla) {
        for (int i = 0; i < qntCaixa; i++) {
            TextField textField = new TextField("", skin, "default");
            BitmapFont font = new BitmapFont(Gdx.files.internal(arquivoFnt), Gdx.files.internal(arquivoPng), false);
            textField.getStyle().font = font; // Define a fonte do TextField.
            textField.getStyle().fontColor = Color.WHITE; // Ajusta a cor do texto.
            textField.getStyle().font.getData().setScale(1.5f, 1.2f);

            TextField.TextFieldStyle style = textField.getStyle();
            style.background.setLeftWidth(15);   // Padding esquerdo
            style.background.setRightWidth(15);  // Padding direito
            style.background.setTopHeight(5);    // Padding superior
            style.background.setBottomHeight(5); // Padding inferior
            textField.setStyle(style);
    
            // Define o listener para capturar o texto.
            final int index = i; // Necessário para o listener interno.
            textField.setTextFieldListener((tf, c) -> {
                if (c != '\n') {
                    tf.setText(tf.getText().toUpperCase());
                    tf.setCursorPosition(tf.getText().length()); // Mantém o cursor no final.
                    textos[index] = tf.getText(); // Salva o texto em maiúsculas.
                    if (tecla != null) tecla.play(1.0f);
                }
            });
    
            textFields.add(textField); // Adiciona o TextField à lista.
            stage.addActor(textField); // Adiciona o TextField à stage.
        }
    }

    public void draw() {
        // Atualiza e desenha a stage.
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        // Atualiza o viewport com as novas dimensões da tela.
        stage.getViewport().update(width, height, true);
    }

    public void desativaCaixas(boolean desativa) {
        for(TextField text : textFields) text.setDisabled(desativa);
    }

    public String[] getTextos() {
        return textos;
    }

    public Stage getStage() {
        return stage;
    }

    public ArrayList<TextField> getTextFields() {
        return textFields;
    }

    public String getMessageText(int index) {
        if (index >= 0 && index < qntCaixa) 
            return this.textos[index]; 
        System.err.println("Index out of range.");
        return null;
    }

    public String getTexto(int index) {
        if (index >= 0 && index < qntCaixa) 
            return this.textos[index];   
        System.err.println("Index out of range.");
        return null;
    }

    public void setMessageText(String messageText, int index) {
        if (index >= 0 && index < qntCaixa) 
            this.textFields.get(index).setMessageText(messageText);
        else   
            System.err.println("Index out of range.");
    }

    public void setTexto(String texto, int index) {
        if (index >= 0 && index < qntCaixa) 
            this.textos[index] = texto;
        else   
            System.err.println("Index out of range.");
    }

    public void setTamanhos(int index, float x, float y, float width, float height) {
        if (index >= 0 && index < qntCaixa) {
            textFields.get(index).setPosition(x, y); 
            textFields.get(index).setSize(width, height);
        }
        else   
            System.err.println("Index out of range.");
    }

    public void setLimiteCaracteres(int index, int limiteCaracteres) {
        if (index >= 0 && index < qntCaixa)
            textFields.get(index).setTextFieldFilter((textField1, c) -> textField1.getText().trim().length() < limiteCaracteres);
        else   
            System.err.println("Index out of range.");
    }
    
    public void setColor(int index, Color cor) {
        if (index >= 0 && index < qntCaixa)
            textFields.get(index).getStyle().fontColor = cor; // Ajuste a cor do texto.
        else   
            System.err.println("Index out of range.");
    }

    public void setScale(int index, float scaleX, float scaleY) {
        if (index >= 0 && index < qntCaixa)
            textFields.get(index).getStyle().font.getData().setScale(scaleX, scaleY);
        else   
            System.err.println("Index out of range.");
    }

    public void setScale(int index, float scale) {
        if (index >= 0 && index < qntCaixa)
            textFields.get(index).getStyle().font.getData().setScale(scale);
        else   
            System.err.println("Index out of range.");
    }
}
