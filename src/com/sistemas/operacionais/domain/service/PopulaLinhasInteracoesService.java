package com.sistemas.operacionais.domain.service;

import com.sistemas.operacionais.domain.model.FilesContextControl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Semaphore;

// Ler o arquivo de interações e popular no Map
public class PopulaLinhasInteracoesService extends Thread { // Herda de thread para permitir a execução em uma thread desatachada da thread principal do programa

    private final SalaCinemaAdicionaInteracoesService salaCinemaAdicionaInteracoesService;
    private final Semaphore semaforo;
    private final List<String> interacoes;
    private final int idPontoVenda;
    private final StringBuilder horariosBuilder;

    public PopulaLinhasInteracoesService(Semaphore semaforo, SalaCinemaAdicionaInteracoesService salaCinemaAdicionaInteracoesService, List<String> interacoes, int idPontoVenda, StringBuilder sb) {
        this.salaCinemaAdicionaInteracoesService = salaCinemaAdicionaInteracoesService;
        this.semaforo = semaforo;
        this.interacoes = interacoes;
        this.idPontoVenda = idPontoVenda;
        this.horariosBuilder = sb;
    }

    @Override
    public void run() {
        try {
            adicionaInteracoesSalaCinema(); // Adiciona as interações na sala de cinema
            semaforo.acquire(); //Bloqueia semaforo
            // Adiciona no final do relatório a informação sobre o horário final de processamento do determinado ponto de venda
            horariosBuilder.append(String.format("Ponto - %d: %s\n", idPontoVenda, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        } catch (InterruptedException | IOException e) {
            e.printStackTrace(); // Exibe stackTrace do erro
        } finally {
            semaforo.release(); //Libera semaforo
        }
    }

    private void adicionaInteracoesSalaCinema() throws IOException {
        adicionaInteracoes(interacoes); // Chama o mpetodo responsável por iterar na lista de strings e adicionar as interações
    }

    private void adicionaInteracoes(List<String> linhas) {
        linhas.forEach(linha -> {
            if (linha.toCharArray()[0] == '#') { //Ignora os comentários no arquivo de interação
                FilesContextControl.incrementAndGetReadedLine(); // Incrementa número de linhas lidas
                return;
            }
            adicionaInteracao(linha); // Adiciona Interação
        });
    }

    private void adicionaInteracao(String linha) {
        try {
            // Chama o serviço responsável por adicionar a interação e incrementa o contador de interaçõe
            semaforo.acquire();
            //System.out.printf("Interação (- %d%n) - %s - Ponto Venda - %d%n", FilesContextControl.getIdCliente(), linha, idPontoVenda); // Exibe log
            salaCinemaAdicionaInteracoesService.adicionaInteracao(linha, FilesContextControl.incrementAndGetIdCliente(), idPontoVenda);
            FilesContextControl.incrementAndGetReadedLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            semaforo.release();
        }
    }
}
