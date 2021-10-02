package com.sistemas.operacionais;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class Main {

    private static SalaCinema salaCinema;
    private static ConfiguracaoSalaCinema configuracaoSalaCinema;

    public static void main(String[] args) {
        configuracaoSalaCinema = obterConfiguracoesSalaCinema();
    }

    private static ConfiguracaoSalaCinema obterConfiguracoesSalaCinema() {
        List<String> configuracoes = null;
        try {
            configuracoes = Files.readAllLines(new File("src/com/sistemas/operacionais/resources/configuracoes_cinema.txt").toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ConfiguracaoSalaCinema(configuracoes);
    }

    private static SalaCinema obterSalaCinema() {
        List<String> interacoes = null;
        try {
            interacoes = Files.readAllLines(new File("src/com/sistemas/operacionais/resources/interacoes_usuarios.txt").toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SalaCinema();
    }

}
