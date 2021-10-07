package com.sistemas.operacionais.domain;

import java.util.Arrays;
import java.util.List;

public class ConfiguracaoSalaCinema {

    private final int quantidadeFileiras;
    private final int quantidadeCadeirasFileira;
    private final List<String> sessoes;

    private static final char DIVISOR_DIMENSOES = 'X';
    private static final char DIVISOR_SESSOES = ',';

    public ConfiguracaoSalaCinema(List<String> lines) {
        String[] dimensoes = lines.get(0).split(Character.toString(DIVISOR_DIMENSOES));
        quantidadeFileiras = Integer.parseInt(dimensoes[0]);
        quantidadeCadeirasFileira = Integer.parseInt(dimensoes[1]);
        sessoes = Arrays.asList(lines.get(1).trim().split(Character.toString(DIVISOR_SESSOES)));
    }

    public Integer getQuantidadeFileiras() {
        return quantidadeFileiras;
    }

    public Integer getQuantidadeCadeirasFileira() {
        return quantidadeCadeirasFileira;
    }

    public Integer getQuantidadeTotalPoltronas() {
        return quantidadeFileiras * quantidadeCadeirasFileira;
    }

    public List<String> getSessoes() {
        return sessoes;
    }

}
