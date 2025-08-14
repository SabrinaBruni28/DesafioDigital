package io.github.desafiodigital.utils;

import io.github.desafiodigital.models.ConfiguracaoFase;
import io.github.desafiodigital.models.NivelContexto;
import io.github.desafiodigital.models.NivelCarta;
import io.github.desafiodigital.models.Nivel;
import io.github.desafiodigital.models.Fase;
import io.github.cdimascio.dotenv.Dotenv;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import java.text.MessageFormat;
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
    
        // Obtém o atributo do JSON
        String atributoBanco = jsonObject.get(atributo).getAsString();

        // Substitui as variáveis de ambiente, se existirem
        atributoBanco = replaceEnvVariables(atributoBanco);

        return atributoBanco;
    }

    private static String replaceEnvVariables(String input) {
        // Encontra todas as ocorrências de ${VARIAVEL} e substitui pelas variáveis de ambiente

        String variavel = input.replace("${", "").replace("}", "");

        FileHandle fileHandle = Gdx.files.internal("gameConfig/.env");

        // Agora, você pode passar o conteúdo lido para o Dotenv
        Dotenv dotenv = Dotenv.configure()
                              .directory(fileHandle.path())
                              .load(); // Passa o conteúdo manualmente

        String result = dotenv.get(variavel);

        return result;
    }

    public static String arquivosFase(int fase) {
        return MessageFormat.format("gameConfig/Fase{0}/Fase{0}.json", fase);
    }

    public static String arquivosFaseNivel(int fase, int nivel) {
        return MessageFormat.format("gameConfig/Fase{0}/nivel{1}.json", fase, nivel);
    }
}