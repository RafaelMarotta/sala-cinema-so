package com.sistemas.operacionais.domain.model;

public class RelatorioInteracoesItem {
    private String idCliente;
    private String cadeira;
    private String sessao;
    private String status;

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
