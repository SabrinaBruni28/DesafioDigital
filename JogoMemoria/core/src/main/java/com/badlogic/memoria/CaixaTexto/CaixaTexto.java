package com.badlogic.memoria.CaixaTexto;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class CaixaTexto extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;
    private TextField textField;
    private String nomeUsuario = "";

    public CaixaTexto(float x, float y, float width, float height) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Configura o input para o stage

        // Carrega um skin básico
        skin = new Skin(Gdx.files.internal("Fonte/uiskin.json"));

        // Configura o TextField
        setupTextField(x, y, width, height);

        // Adiciona o TextField à stage
        stage.addActor(textField);

        // Força uma chamada para o resize logo após a inicialização
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void setupTextField(float x, float y, float width, float height) {
        textField = new TextField("", skin, "default"); // Passa o estilo "default"
        textField.setMessageText("Digite seu nome"); // Placeholder
        textField.setPosition(x, y); // Define a posição inicial (relativa)
        textField.setSize(width, height); // Define o tamanho inicial da caixa de texto

        BitmapFont font = new BitmapFont(Gdx.files.internal("Fonte/default.fnt"), Gdx.files.internal("Fonte/default.png"), false);
        textField.getStyle().font = font; // Define a fonte do TextField
        textField.getStyle().fontColor = Color.WHITE; // Ajuste a cor do texto
        
        textField.setTextFieldListener((textField, c) -> {
            if (c != '\n') {
                nomeUsuario = textField.getText(); // Obtém o texto digitado
            }
        });
    }

    @Override
    public void render() {
        // Atualiza e desenha a stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Atualiza o viewport com as novas dimensões da tela
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        this.stage.dispose();
        this.skin.dispose();
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
}
