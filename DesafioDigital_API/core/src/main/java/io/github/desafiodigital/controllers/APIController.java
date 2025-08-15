package io.github.desafiodigital.controllers;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.desafiodigital.models.*;

public class APIController {

    private final HttpClient client;
    private final Gson gson;
    public Aluno aluno;
    public Turma turma;
    public Partida partida;
    public NivelPartida nivelPartida;

    public APIController(Aluno aluno, Turma turma, Partida partida, NivelPartida nivelPartida) {
        this.client = HttpClient.newHttpClient();
        this.gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();  
        this.aluno = aluno;
        this.turma = turma;
        this.partida = partida;
        this.nivelPartida = nivelPartida;
    }

    // -------- ALUNO --------
    public Aluno registraOuRetornaAluno() {
        aluno.setTurma(new Turma(turma.getId()));
        String nomeAluno = URLEncoder.encode(aluno.getNome(), StandardCharsets.UTF_8);
        try {
            // 1. Tentar buscar aluno existente pelo nome e turma
            String buscaUrl = String.format("http://localhost:8080/alunos/buscar?nome=%s&turmaId=%d",
                    nomeAluno, aluno.getTurma().getId());
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(URI.create(buscaUrl))
                    .GET()
                    .build();

            HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

            if (getResponse.statusCode() == 200) {
                Aluno[] alunosExistentes = gson.fromJson(getResponse.body(), Aluno[].class);
                if (alunosExistentes.length > 0) {
                    // Aluno já existe, retorna ele
                    aluno.setId(alunosExistentes[0].getId());
                    System.out.println("Aluno encontrado: " + alunosExistentes[0].getNome());
                    return alunosExistentes[0];
                }
            }

            // 2. Se não existe, registra novo aluno
            String json = gson.toJson(aluno);
            System.out.println("JSON enviado para /alunos: " + json);
            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/alunos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

            if (postResponse.statusCode() == 201) {
                Aluno alunoCriado = gson.fromJson(postResponse.body(), Aluno.class);
                aluno.setId(alunoCriado.getId());
                System.err.println("Novo aluno registrado: " + alunoCriado.getNome());
                return alunoCriado;
            } 
            else {
                System.out.println("Erro ao registrar aluno: " + postResponse.body());
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // -------- TURMA --------
    public Turma registraOuRetornaTurma() {
        String nomeTurma = URLEncoder.encode(turma.getNome(), StandardCharsets.UTF_8);
        try {
            // 1. Tentar buscar turma existente pelo nome
            String buscaUrl = String.format("http://localhost:8080/turmas/buscar?nome=%s", nomeTurma);
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(URI.create(buscaUrl))
                    .GET()
                    .build();

            HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

            if (getResponse.statusCode() == 200) {
                Turma[] turmasExistentes = gson.fromJson(getResponse.body(), Turma[].class);
                if (turmasExistentes.length > 0) {
                    // Turma já existe, retorna ela
                    turma.setId(turmasExistentes[0].getId());
                    System.err.println("Turma encontrada: " + turma.getId() + ", " + turmasExistentes[0].getNome());
                    return turmasExistentes[0];
                }
            }

            // 2. Se não existe, registra nova turma
            String json = gson.toJson(turma);
            System.out.println("JSON enviado para /turmas: " + json);
            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/turmas"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

            if (postResponse.statusCode() == 201) {
                Turma turmaCriada = gson.fromJson(postResponse.body(), Turma.class);
                turma.setId(turmaCriada.getId());
                System.err.println("Nova turma registrada: " + turmaCriada.getNome());
                return turmaCriada;
            } 
            else {
                System.out.println("Erro ao registrar turma: " + postResponse.body());
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // -------- PARTIDA --------
    public boolean registraPartida() {
        partida.setAluno(new Aluno(aluno.getId()));
        try {
            String json = gson.toJson(partida);
            System.out.println("JSON enviado para /partidas: " + json);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/partidas"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                Partida partidaCriada = gson.fromJson(response.body(), Partida.class);
                partida.setId(partidaCriada.getId());
                System.err.println("Nova partida registrada: " + partidaCriada.getId());
                return true;
            } 
            else {
                System.out.println("Erro ao registrar partida: " + response.body());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // -------- NÍVEL PARTIDA --------
    public boolean registraNivelPartida() {
        nivelPartida.setPartida(new Partida(partida.getId()));
        try {
            String json = gson.toJson(nivelPartida);
            System.out.println("JSON enviado para /nivel-partidas: " + json);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/nivel-partidas"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                NivelPartida nivelCriado = gson.fromJson(response.body(), NivelPartida.class);
                nivelPartida.setId(nivelCriado.getId());
                System.err.println("Novo nível registrado: " + nivelCriado.getId());
                return true;
            } 
            else {
                System.out.println("Erro ao registrar nível: " + response.body());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}