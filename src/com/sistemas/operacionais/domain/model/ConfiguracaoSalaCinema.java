package com.sistemas.operacionais.domain.model;

import java.util.List;

public class ConfiguracaoSalaCinema {
    private final int quantidadeFileiras;
    private final int quantidadeCadeirasFileira;
    private final List<String> sessoes;

    public ConfiguracaoSalaCinema(int quantidadeFileiras, int quantidadeCadeirasFileira, List<String> sessoes) {
        this.quantidadeFileiras = quantidadeFileiras;
        this.quantidadeCadeirasFileira = quantidadeCadeirasFileira;
        this.sessoes = sessoes;
    }

    public Integer obterQuantidadeFileiras() {
        return quantidadeFileiras;
    }
    public Integer obterQuantidadeCadeirasFileira() {
        return quantidadeCadeirasFileira;
    }
    public Integer obterQuantidadeTotalPoltronas() {
        return quantidadeFileiras * quantidadeCadeirasFileira;
    }
    public List<String> obterSessoes() {
        return sessoes;
    }
}
