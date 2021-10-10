package com.sistemas.operacionais.domain.builder;

import com.sistemas.operacionais.domain.model.ConfiguracaoSalaCinema;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ConfiguracaoSalaCinemaBuilder {
    private static final char DIVISOR_DIMENSOES = 'X';
    private static final char DIVISOR_SESSOES = ',';

    public static ConfiguracaoSalaCinema build(List<String> lines) {
        String[] dimensoes = trataString(lines.get(0)).split(Character.toString(DIVISOR_DIMENSOES));
        int quantidadeFileiras = Integer.parseInt(dimensoes[0]);
        int quantidadeCadeirasFileira = Integer.parseInt(dimensoes[1]);
        List<String> sessoes = Arrays.asList(trataString(lines.get(1)).split(Character.toString(DIVISOR_SESSOES)));
        return new ConfiguracaoSalaCinema(quantidadeFileiras, quantidadeCadeirasFileira, sessoes);
    }

    private static String trataString(String string) {
        return string.toUpperCase(Locale.ROOT).substring(0, string.indexOf("#")).replaceAll(" ", "");
    }
}
