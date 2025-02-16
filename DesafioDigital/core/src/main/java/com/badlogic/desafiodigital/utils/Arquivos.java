package com.badlogic.desafiodigital.utils;

import com.badlogic.desafiodigital.models.ConfiguracaoFase;
import com.badlogic.desafiodigital.models.NivelContexto;
import com.badlogic.desafiodigital.models.NivelCarta;
import com.badlogic.desafiodigital.models.Nivel;
import com.badlogic.desafiodigital.models.Fase;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import java.util.ArrayList;

/** Classe para manipular e ler arquivos. */
public abstract class Arquivos {
    // Arquivos de fonte.
    private static String arquivoJson = "Fonte/uiskin.json";
    private static String arquivoFnt = "Fonte/default.fnt";
    private static String arquivoPng = "Fonte/default.png";

    public static String getArquivoJson() {
        return arquivoJson;
    }

    public static String getArquivoFnt() {
        return arquivoFnt;
    }

    public static String getArquivoPng() {
        return arquivoPng;
    }

    public static Nivel readNivelFromFile(String filePath, String tipoNivel) {
        // Usa o Gdx.files.internal para carregar o arquivo
        FileHandle fileHandle = Gdx.files.internal(filePath);
        String content = fileHandle.readString(); // lê o conteúdo do arquivo como uma string

        Gson gson = new Gson();

        if (tipoNivel.equals("cartas")) {
            return gson.fromJson(content, NivelCarta.class);
        } 
        else if (tipoNivel.equals("contexto")) {
            return gson.fromJson(content, NivelContexto.class);
        } 
        else {
            throw new IllegalArgumentException("Tipo de nível desconhecido: " + tipoNivel);
        }
    }

    public static Fase readFaseFromFile(String filePath) {
        // Usa o Gdx.files.internal para carregar o arquivo
        FileHandle fileHandle = Gdx.files.internal(filePath);
        String content = fileHandle.readString(); // lê o conteúdo do arquivo como uma string

        Gson gson = new Gson();

        return gson.fromJson(content, Fase.class);
    }
    
    public static ConfiguracaoFase readConfiguracaoFaseFromFile(String filePath) {
        // Usa o Gdx.files.internal para carregar o arquivo
        FileHandle fileHandle = Gdx.files.internal(filePath);
        String content = fileHandle.readString(); // lê o conteúdo do arquivo como uma string

        Gson gson = new Gson();

        return gson.fromJson(content, ConfiguracaoFase.class);
    }

    public static ArrayList<Color> readCoresFromFile(String filePath) {
        // Usa o Gdx.files.internal para carregar o arquivo
        FileHandle fileHandle = Gdx.files.internal(filePath);
        String content = fileHandle.readString(); // lê o conteúdo do arquivo como uma string

        // Parse o conteúdo para um JsonObject
        JsonObject jsonObject = JsonParser.parseString(content).getAsJsonObject();
        
        // Agora, obtém o array de cores corretamente usando o JsonArray da Gson
        JsonArray coresJsonArray = jsonObject.getAsJsonArray("cores");

        ArrayList<Color> cores = new ArrayList<>();

        // Itera sobre o array de cores e converte cada string para uma cor
        for (int i = 0; i < coresJsonArray.size(); i++) {
            String colorString = coresJsonArray.get(i).getAsString();

            // Remove o prefixo "0x" se presente
            if (colorString.startsWith("0x")) {
                colorString = colorString.substring(2);  // Remove os dois primeiros caracteres "0x"
            }

            // Converte a string hex para o tipo Color
            Color color = Color.valueOf(colorString);
            cores.add(color);
        }

        return cores;
    }
    
    public static String readBancoDeDadosFromFile(String filePath, String atributo) {
        // Usa o Gdx.files.internal para carregar o arquivo
        FileHandle fileHandle = Gdx.files.internal(filePath);
        String content = fileHandle.readString(); // lê o conteúdo do arquivo como uma string

        // Parse o conteúdo para um JsonObject
        JsonObject jsonObject = JsonParser.parseString(content).getAsJsonObject();
    
        // Agora, obtém o array de cores corretamente usando o JsonArray da Gson
        String atributoBanco = jsonObject.get(atributo).getAsString();

        return atributoBanco;
    }

    public static String arquivosFase(int fase) {
        return switch (fase) {
            case 1 -> "gameConfig/Fase1/Fase1.json";
            case 2 -> "gameConfig/Fase2/Fase2.json";
            case 3 -> "gameConfig/Fase3/Fase3.json";
            default -> null;
        };
    }

    public static String arquivosFaseNivel(int fase, int nivel) {
        return switch (fase) {
            case 1 -> arquivosFase1(nivel);
            case 2 -> arquivosFase2(nivel);
            case 3 -> arquivosFase3(nivel);
            default -> null;
        };
    }

    private static String arquivosFase1(int nivel) {
        return switch (nivel) {
            case 1 -> "gameConfig/Fase1/nivel1.json";
            case 2 -> "gameConfig/Fase1/nivel2.json";
            case 3 -> "gameConfig/Fase1/nivel3.json";
            default -> null;
        };
    }

    private static String arquivosFase2(int nivel) {
        return switch (nivel) {
            case 1 -> "gameConfig/Fase2/nivel1.json";
            case 2 -> "gameConfig/Fase2/nivel2.json";
            case 3 -> "gameConfig/Fase2/nivel3.json";
            default -> null;
        };
    }

    private static String arquivosFase3(int nivel) {
        return switch (nivel) {
            case 1 -> "gameConfig/Fase3/nivel1.json";
            case 2 -> "gameConfig/Fase3/nivel2.json";
            case 3 -> "gameConfig/Fase3/nivel3.json";
            case 4 -> "gameConfig/Fase3/nivel4.json";
            default -> null;
        };
    }
}
