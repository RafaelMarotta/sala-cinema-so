package com.sistemas.operacionais;

import com.sistemas.operacionais.domain.builder.ConfiguracaoSalaCinemaBuilder;
import com.sistemas.operacionais.domain.builder.RelatorioInteracoesBuilder;
import com.sistemas.operacionais.domain.builder.SalaCinemaBuilder;
import com.sistemas.operacionais.domain.model.ConfiguracaoSalaCinema;
import com.sistemas.operacionais.domain.model.RelatorioInteracoes;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.service.SalaCinemaAdicionaInteracoesService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    private static SalaCinemaAdicionaInteracoesService salaCinemaAdicionaInteracoesService;

    public static void main(String[] args) throws IOException {
        ConfiguracaoSalaCinema configuracaoSalaCinema = obterConfiguracoesSalaCinema();
        SalaCinema salaCinema = SalaCinemaBuilder.builder(configuracaoSalaCinema).build();
        salaCinemaAdicionaInteracoesService = new SalaCinemaAdicionaInteracoesService(salaCinema, configuracaoSalaCinema);
        adicionaInteracoesSalaCinema();
        RelatorioInteracoes relatorioInteracoes = RelatorioInteracoesBuilder.builder(salaCinema).build();
        Files.writeString(Path.of("src/com/sistemas/operacionais/resources/vendas.txt"), relatorioInteracoes.toString());
    }

    private static ConfiguracaoSalaCinema obterConfiguracoesSalaCinema() throws IOException {
        List<String> configuracoes = Files.readAllLines(obterArquivoConfiguracaoSala().toPath(), StandardCharsets.UTF_8);
        return ConfiguracaoSalaCinemaBuilder.build(configuracoes);
    }

    private static void adicionaInteracoesSalaCinema() throws IOException {
        List<String> interacoes = Files.readAllLines(obterArquivoInteracoes().toPath(), StandardCharsets.UTF_8);
        adicionaInteracoes(interacoes);
    }

    private static void adicionaInteracoes(List<String> interacoes) {
        for(int i = 0; i< interacoes.size(); i++) {
            salaCinemaAdicionaInteracoesService.adicionaInteracao(interacoes.get(i), i);
        }
    }

    private static File obterArquivoConfiguracaoSala() {
        return new File("src/com/sistemas/operacionais/resources/configuracoes_cinema.txt");
    }

    private static File obterArquivoInteracoes() {
        return new File("src/com/sistemas/operacionais/resources/interacoes_usuarios.txt");
    }
}
