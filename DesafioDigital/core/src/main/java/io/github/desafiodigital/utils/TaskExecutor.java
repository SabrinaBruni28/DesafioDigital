package io.github.desafiodigital.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.HashMap;

/** Classe para executar tarefas assincronamente. */
public class TaskExecutor {

    // Threads sem limite de quantidade.
    private static ExecutorService executor = Executors.newCachedThreadPool();
    // Mapa para armazenar tarefas com nome.
    private static HashMap<String, Runnable> tasks = new HashMap<>();

    // Método estático para criar e armazenar a tarefa com um nome.
    public static void createTask(String taskName, Runnable task) {
        tasks.put(taskName, task);  // Armazena a tarefa no mapa com o nome como chave.
    }

    // Método estático para deletar a tarefa com um nome.
    public static void deleteTask(String taskName) {
        tasks.remove(taskName);  // Deleta a tarefa no mapa com o nome como chave.
    }

    // Método estático para executar uma tarefa pelo nome.
    public static void executeTask(String taskName) {
        Runnable task = tasks.get(taskName);  // Recupera a tarefa pelo nome.

        if (task != null) executor.submit(task);  // Envia a tarefa para execução assíncrona.
        else System.out.println("Tarefa com nome '" + taskName + "' não encontrada.");
    }

    // Método estático para cancelar todas as tarefas pendentes.
    public static void cancelTasks() {
        executor.shutdownNow();  // Interrompe todas as tarefas pendentes.
    }

    public static void dispose() {
        executor.shutdown(); // Solicita o encerramento das tarefas atuais
        try {
            if (!executor.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                System.out.println("Forçando o encerramento das tarefas...");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.err.println("Erro ao aguardar o encerramento das tarefas: " + e.getMessage());
            executor.shutdownNow();
        }
    }
}

/* // Exemplo de uma tarefa simples.
        Runnable task1 = () -> {
            try {
                System.out.println("Tarefa 1 iniciada...");
                Thread.sleep(2000);  // Simula uma tarefa que leva 2 segundos.
                System.out.println("Tarefa 1 concluída.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        // Criando e armazenando as tarefas com nome
        createTask("Tarefa1", task1);

        // Executando a tarefa.
        executeTask("Tarefa1"); 
*/