package com.sistemas.operacionais.domain.model;

// Modelo que representa o item do relatório de interações
public class RelatorioInteracoesItem {
    private final String idCliente;
    private final String cadeira;
    private final String sessao;
    private final String status;

    public RelatorioInteracoesItem(String idCliente, String cadeira, String sessao, String status) {
        this.idCliente = idCliente;
        this.cadeira = cadeira;
        this.sessao = sessao;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s   %s   %s   %s", idCliente, cadeira, sessao, status);
    }
}
