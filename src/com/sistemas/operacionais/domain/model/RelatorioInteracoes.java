package com.sistemas.operacionais.domain.model;

import java.util.List;

// Modelo que representa o relatório completo de interações
public class RelatorioInteracoes {
    private final List<RelatorioInteracoesItem> interacoes;

    public RelatorioInteracoes(List<RelatorioInteracoesItem> interacoes) {
        this.interacoes = interacoes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (RelatorioInteracoesItem interacao : interacoes) {
            sb.append(interacao.toString()+"\n");
        }
        return sb.toString();
    }
}
