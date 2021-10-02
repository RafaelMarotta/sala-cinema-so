package com.sistemas.operacionais;

import java.util.Arrays;
import java.util.List;

public class ConfiguracaoSalaCinema {

    private final int quantidadeFileiras;
    private final int quantidadeCadeiras;
    private final List<String> sessoes;

    private static final char DIVISOR_DIMENSOES = 'X';
    private static final char DIVISOR_SESSOES = ',';

    public ConfiguracaoSalaCinema(List<String> lines) {
        String[] dimensoes = lines.get(0).split(Character.toString(DIVISOR_DIMENSOES));
        quantidadeFileiras = Integer.parseInt(dimensoes[0]);
        quantidadeCadeiras = Integer.parseInt(dimensoes[1]);
        sessoes = Arrays.asList(lines.get(1).trim().split(Character.toString(DIVISOR_SESSOES)));
    }

    public int getQuantidadeFileiras() {
        return quantidadeFileiras;
    }

    public int getQuantidadeCadeiras() {
        return quantidadeCadeiras;
    }

    public List<String> getSessoes() {
        return sessoes;
    }

}
