package com.sistemas.operacionais.domain.service;

import com.sistemas.operacionais.domain.builder.ConfiguracaoSalaCinemaBuilder;
import com.sistemas.operacionais.domain.model.ConfiguracaoSalaCinema;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// Lê o arquivo de configurações e converte para o modelo "ConfiguracaoSalaCinema"
public class ObtemConfiguracoesSalaCinemaService {
    private static final String ARQUIVO_CONFIGURACOES = "src/com/sistemas/operacionais/resources/configuracoes_cinema.txt";

    // Lê o arquivo de configurações e converte para o modelo "ConfiguracaoSalaCinema"
    public static ConfiguracaoSalaCinema obterConfiguracoesSalaCinema() throws IOException {
        List<String> configuracoes = Files.readAllLines(obterArquivoConfiguracaoSalaPath(), StandardCharsets.UTF_8);
        return ConfiguracaoSalaCinemaBuilder.build(configuracoes);
    }

    private static Path obterArquivoConfiguracaoSalaPath() {
        return new File(ARQUIVO_CONFIGURACOES).toPath();
    }
}
