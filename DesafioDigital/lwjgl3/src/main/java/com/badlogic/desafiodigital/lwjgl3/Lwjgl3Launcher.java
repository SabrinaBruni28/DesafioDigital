package com.badlogic.desafiodigital.lwjgl3;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.desafiodigital.DesafioDigital;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    /**
    * The entry point of the application.
    *
    * @param args the command line arguments
    */
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new DesafioDigital(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("DesafioDigital");

        // Obter a resolução da tela do monitor principal
        DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
        int screenWidth = displayMode.width;
        int screenHeight = displayMode.height;

        // Definir a janela com 80% da largura e altura da tela
        configuration.setWindowedMode((int)(screenWidth * 0.8), (int)(screenHeight * 0.8));
        configuration.setWindowSizeLimits(400, 300, -1, -1);

        configuration.useVsync(true);
        configuration.setForegroundFPS(displayMode.refreshRate + 1);

        // Ícones da janela
        configuration.setWindowIcon("icone128.png", "icone64.png", "icone32.png", "icone16.png");

        return configuration;
    }
}