package com.sistemas.operacionais.domain.model.enums;

import com.sistemas.operacionais.domain.model.InteracaoUsuario;

// Enum para definir os tipos de ação do usuário e sua descrição
public enum UltimaAcaoEnum {
    CONSULTA("Apenas consultou"),
    SELECAO("Não confirmou o pagamento"),
    PAGAMENTO("Confirmou"),
    ACAO_INVALIDA("Ação inválida");

    private String status;

    public String obterStatus() {
        return status;
    }

    UltimaAcaoEnum(String status) {
        this.status = status;
    }

    // Obtém a última ação baseada nas letras que vêm do arquivo
    public static UltimaAcaoEnum obterAcao(String comportamento) {
        switch (comportamento) {
            case "CXX":
                return CONSULTA;
            case "CSX":
                return SELECAO;
            case "CSP":
                return PAGAMENTO;
            default:
                return ACAO_INVALIDA;
        }
    }

    // Retorna se o fluxo foi concluído caso a última interação do usuário seja de pagamento
    public static boolean ehFluxoReservaCompleto(InteracaoUsuario interacaoUsuario) {
        return interacaoUsuario.obterUltimaAcao().equals(UltimaAcaoEnum.PAGAMENTO);
    }

}
