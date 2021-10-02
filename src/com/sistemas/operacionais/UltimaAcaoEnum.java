package com.sistemas.operacionais;

public enum UltimaAcaoEnum {
    CONSULTA,
    SELECAO,
    PAGAMENTO,
    ACAO_INVALIDA;

    public static UltimaAcaoEnum obterAcao(String comportamento) {
        switch (comportamento) {
            case "CSP":
                return CONSULTA;
            case "CSX":
                return SELECAO;
            case "CXX":
                return PAGAMENTO;
            default:
                return ACAO_INVALIDA;
        }
    }
}
