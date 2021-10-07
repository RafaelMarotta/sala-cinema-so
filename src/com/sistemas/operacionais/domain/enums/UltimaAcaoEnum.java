package com.sistemas.operacionais.domain.enums;

import com.sistemas.operacionais.domain.InteracaoUsuario;

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

    public static boolean ehFluxoReservaCompleto(InteracaoUsuario interacaoUsuario) {
        return interacaoUsuario.getUltimaAcao().equals(UltimaAcaoEnum.PAGAMENTO);
    }

}
