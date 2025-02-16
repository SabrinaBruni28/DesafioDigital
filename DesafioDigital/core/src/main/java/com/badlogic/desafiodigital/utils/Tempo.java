package com.badlogic.desafiodigital.utils;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.Gdx;

/** Classe responsável por contar tempo. */
public abstract class Tempo {
    private static float elapsedTime = 0f;  // Tempo acumulado
    private static boolean isRunning = false;  // Flag para controlar se a contagem está ativa

    public static void start() {
        isRunning = true;
    }
    
    public static void pause() {
        isRunning = false;
    }
    
    public static void reset() {
        elapsedTime = 0f;
        isRunning = false;
    }

    public static void update(float deltaTime) {
        if (isRunning) {
            elapsedTime += deltaTime;  // Incrementa o tempo somente se o temporizador estiver ativo
        }
    }

    public static float getElapsedTime() {
        return elapsedTime;
    }

    public static void render() {
        if (isRunning) {
            float deltaTime = Gdx.graphics.getDeltaTime();
            update(deltaTime);
        }
    }

    /**
     * Executa uma ação com atraso.
     *
     * @param delayInSeconds Tempo de atraso em segundos.
     * @param action         Ação a ser executada após o atraso.
     */
    public static void executarComDelay(float delayInSeconds, Runnable action) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                action.run();
            }
        }, delayInSeconds);
    }
}
