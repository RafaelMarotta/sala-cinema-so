package com.sistemas.operacionais.domain.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// Ler o arquivo de interações e popular no Map
public class PopulaArquivoInteracoesService {

    private final SalaCinemaAdicionaInteracoesService salaCinemaAdicionaInteracoesService;
    private static final String ARQUIVO_INTERACOES = "src/com/sistemas/operacionais/resources/interacoes_usuarios.txt";

    public PopulaArquivoInteracoesService(SalaCinemaAdicionaInteracoesService salaCinemaAdicionaInteracoesService) {
        this.salaCinemaAdicionaInteracoesService = salaCinemaAdicionaInteracoesService;
    }

    public void adicionaInteracoesSalaCinema() throws IOException {
        // Lê todas as linhas do arquivo de interações e salva em uma lista de strings
        List<String> interacoes = Files.readAllLines(obterPathArquivoInteracoes(), StandardCharsets.UTF_8);
        adicionaInteracoes(interacoes); // Chama o mpetodo responsável por iterar na lista de strings e adicionar as interações
    }

    private void adicionaInteracoes(List<String> linhas) {
        AtomicInteger quantidadeInteracoes = new AtomicInteger(); // Utilizado pq estamos manipulando a variavel dentro de uma expressão lambda
        linhas.forEach(linha -> {
            if (linha.toCharArray()[0] == '#') { //Ignora os comentários no arquivo de interação
                return;
            }
            // Chama o serviço responsável por adicionar a interação e incrementa o contador de interações
            salaCinemaAdicionaInteracoesService.adicionaInteracao(linha, quantidadeInteracoes.getAndIncrement());
        });
    }

    // Obtém o path do arquivo de interações
    private Path obterPathArquivoInteracoes() {
        return new File(ARQUIVO_INTERACOES).toPath();
    }
}
