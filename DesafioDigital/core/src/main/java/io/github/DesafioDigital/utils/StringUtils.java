package io.github.DesafioDigital.utils;

import java.util.regex.Pattern;
import java.util.ArrayList;

/** Classe de ferramentas para Strings. */
public abstract class StringUtils {

    /** Confere se a string é nula. */
    public static boolean isNulo(String texto) {
        return texto == null;
    }

    /** Confere se a string está vazia. */
    public static boolean isVazio(String texto) {
        return texto.trim().isEmpty();
    }

    /** Confere se a string possui algum número. */
    public static boolean hasNumeros(String texto) {
        return Pattern.compile("[0-9]").matcher(texto).find();
    }

    /** Confere se a string possui algum caractere especial. */
    public static boolean hasCaracteresEspeciais(String texto) {
        return Pattern.compile("[*+=\\-/\\\\,;:.?!°ºª_&%$¬#@§\"'\\[\\]{}()]").matcher(texto).find();
    } 

    /** Confere se a string possui pelo menos N caracteres. */
    public static boolean hasPeloMenosNCaracteres(String texto, int N){
        return texto.trim().length() >= N;
    }

    /** Retorna a string com \n a cada intervalo. */
    public static String addQuebraLinha(String text, int interval) {
        StringBuilder result = new StringBuilder();
        int length = text.length();
        int start = 0;
        
        while (start < length) {
            // Calcula o limite final, sem ultrapassar o comprimento total.
            int end = Math.min(start + interval, length);
            
            // Procura o último espaço antes do limite.
            if (end < length && !Character.isWhitespace(text.charAt(end))) {
                int lastSpace = text.lastIndexOf(" ", end);
                if (lastSpace > start) {
                    end = lastSpace;
                }
            }
            
            // Adiciona o segmento atual e a quebra de linha.
            result.append(text, start, end).append("\n");
            
            // Avança o início para o próximo segmento.
            start = end + 1;
        }
        
        return result.toString();
    }

    /** Retorna a string com \n a cada ponto. */
    public static String addQuebraLinhaEmPontos(String text) {
        String texto;
        texto = text.replace(".", ".\n");
        texto = texto.replace("!", "!\n");
        texto = texto.replace("?", "?\n");
        texto = texto.replace(";", ";\n");
        return texto;
    }

    /** Retorna a string com \n a cada linha e a cada intervalo. */
    public static String addQuebraLinhaComLimite(String text, int maxLineLength) {
        StringBuilder result = new StringBuilder();

        // Conjunto de delimitadores para verificar no final das sentenças.
        String delimitadores = ".!?;";

        // Divide o texto nas sentenças separadas pelos delimitadores.
        String[] sentences = text.split("[.!?;]");

        for (String sentence : sentences) {
            sentence = sentence.trim(); // Remove espaços extras.

            // Divide a sentença em partes se ultrapassar o limite de caracteres.
            while (sentence.length() > maxLineLength) {
                // Encontra o último espaço dentro do limite de tamanho.
                int breakPoint = sentence.lastIndexOf(" ", maxLineLength);

                // Se não houver espaço, quebra exatamente no limite.
                if (breakPoint == -1) breakPoint = maxLineLength;

                result.append(sentence, 0, breakPoint).append("\n");

                // Reduz a sentença, começando do ponto após o breakPoint.
                sentence = sentence.substring(breakPoint).trim();
            }

            // Adiciona a sentença (ou parte final dela).
            if (!sentence.isEmpty()) {
                result.append(sentence);

                // Verifica qual delimitador original foi usado e o adiciona.
                for (char delimitador : delimitadores.toCharArray()) {
                    String delimitadorComSentenca = sentence + delimitador;
                    if (text.contains(delimitadorComSentenca)) {
                        result.append(delimitador);
                        break;
                    }
                }

                result.append("\n");
            }
        }

        return result.toString();
    }

    /** Retorna a string com \n a cada linha e a cada intervalo, com o texto centralizado. */
    public static String addQuebraLinhaComLimiteCentralizado(String text, int maxLineLength) {
        StringBuilder result = new StringBuilder();
        
        // Divide o texto nas sentenças separadas por pontos.
        String[] sentences = text.split("\\.");
        
        for (String sentence : sentences) {
            sentence = sentence.trim(); // Remove espaços extras no começo e no final
            
            // Lista para armazenar as linhas temporárias
            ArrayList<String> lines = new ArrayList<>();
            
            // Divide a sentença em partes se ultrapassar o limite de caracteres.
            while (sentence.length() > maxLineLength) {
                // Encontra o último espaço dentro do limite de tamanho.
                int breakPoint = sentence.lastIndexOf(" ", maxLineLength);
                
                // Se não houver espaço, quebra exatamente no limite.
                if (breakPoint == -1) breakPoint = maxLineLength;
                
                lines.add(sentence.substring(0, breakPoint).trim());
                
                // Reduz a sentença, começando do ponto após o breakPoint.
                sentence = sentence.substring(breakPoint).trim();
            }
            
            // Adiciona a última parte da sentença se houver.
            if (!sentence.isEmpty()) {
                lines.add(sentence);
            }
            
            // Centraliza cada linha antes de adicionar ao resultado
            for (String line : lines) {
                result.append(centerLine(line, maxLineLength)).append("\n");
            }
        }
        
        return result.toString();
    }

    /** Retorna a string centralizada dentro de um limite de caracteres. */
    private static String centerLine(String line, int maxLineLength) {
        int spacesToAdd = maxLineLength - line.length();
        
        // Se a linha for maior ou igual ao limite, retorna a linha sem alteração
        if (spacesToAdd <= 0) {
            return line;
        }
        
        // Divide os espaços restantes em duas partes, uma para a esquerda e outra para a direita
        int leftPadding = spacesToAdd / 2;
        int rightPadding = spacesToAdd - leftPadding;
        
        // Cria a linha com o espaçamento adequado
        StringBuilder centeredLine = new StringBuilder();
        
        // Adiciona os espaços à esquerda
        for (int i = 0; i < leftPadding; i++) {
            centeredLine.append(" ");
        }
        
        // Adiciona o texto original
        centeredLine.append(line);
        
        // Adiciona os espaços à direita
        for (int i = 0; i < rightPadding; i++) {
            centeredLine.append(" ");
        }
        
        return centeredLine.toString();
    }

    /** Retorna a string com \n a cada linha e a cada intervalo, com o texto justificado. */
    public static String addQuebraLinhaComLimiteJustificado(String text, int maxLineLength) {
        StringBuilder result = new StringBuilder();
        
        // Divide o texto nas sentenças separadas por pontos.
        String[] sentences = text.split("\\.");
        
        for (String sentence : sentences) {
            sentence = sentence.trim(); // Remove espaços extras no começo e no final
            
            // Lista para armazenar as linhas temporárias
            ArrayList<String> lines = new ArrayList<>();
            
            // Divide a sentença em partes se ultrapassar o limite de caracteres.
            while (sentence.length() > maxLineLength) {
                // Encontra o último espaço dentro do limite de tamanho.
                int breakPoint = sentence.lastIndexOf(" ", maxLineLength);
                
                // Se não houver espaço, quebra exatamente no limite.
                if (breakPoint == -1) breakPoint = maxLineLength;
                
                lines.add(sentence.substring(0, breakPoint).trim());
                
                // Reduz a sentença, começando do ponto após o breakPoint.
                sentence = sentence.substring(breakPoint).trim();
            }
            
            // Adiciona a última parte da sentença se houver.
            if (!sentence.isEmpty()) {
                lines.add(sentence);
            }
            
            // Justifica cada linha.
            for (String line : lines) {
                result.append(justifyLine(line, maxLineLength)).append("\n");
            }
        }
        
        return result.toString();
    }

    /**
     * Justifica todas as linhas de texto separadas por '\n',
     * preservando os espaços existentes e justificando de forma proporcional.
     * @param text Texto contendo várias linhas separadas por '\n'.
     * @param maxLineLength Comprimento máximo para cada linha.
     * @return Texto com todas as linhas justificadas.
     */
    public static String justificarTexto(String text, int maxLineLength) {
        StringBuilder result = new StringBuilder();

        // Divide o texto em linhas usando '\n'
        String[] lines = text.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            // Justifica linhas intermediárias, mas deixa a última linha alinhada à esquerda
            if (i == lines.length - 1) {
                result.append(line); // Não justifica a última linha do parágrafo
            } 
            else {
                result.append(justifyLine(line, maxLineLength));
                result.append("\n");
            }
        }

        return result.toString();
    }

    /**
     * Justifica uma única linha, preservando os espaços existentes e distribuindo extras proporcionalmente.
     * @param line Linha de texto a ser justificada.
     * @param maxLineLength Comprimento máximo para a linha.
     * @return Linha justificada.
     */
    private static String justifyLine(String line, int maxLineLength) {
        if (line.length() >= maxLineLength || !line.contains(" ")) {
            return line; // Não justifica se já está no limite ou é uma única palavra
        }

        // Divide a linha em palavras mantendo os espaços existentes
        String[] words = line.split(" ");
        int currentLength = line.length();

        // Calcula o número de espaços extras a serem distribuídos
        int spacesToAdd = maxLineLength - currentLength;
        int gaps = words.length - 1;

        // Distribui os espaços proporcionalmente entre os intervalos
        int spacesPerGap = spacesToAdd / gaps;
        int extraSpaces = spacesToAdd % gaps;

        // Reconstrói a linha com os espaços distribuídos
        StringBuilder justifiedLine = new StringBuilder(words[0]);

        for (int i = 1; i < words.length; i++) {
            // Adiciona espaços extras para este intervalo
            int currentSpaceCount = spacesPerGap + (i <= extraSpaces ? 1 : 0);
            justifiedLine.append(" ".repeat(currentSpaceCount)).append(words[i]);
        }

        return justifiedLine.toString();
    }
}
