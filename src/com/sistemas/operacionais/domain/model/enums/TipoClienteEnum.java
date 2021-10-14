package com.sistemas.operacionais.domain.model.enums;

import com.sistemas.operacionais.domain.model.InteracaoUsuario;

// Enum para definir os tipos de cliente
public enum TipoClienteEnum {
    REGULAR,
    MEIA_ENTRADA,
    CLUBE_CINEMA,
    TIPO_CLIENTE_INVALIDO;

    // Obtém um tipo de cliente de acordo com as letras identificadores que vêm do arquivo
    public static TipoClienteEnum obterTipoCliente(String tipoCliente) {
        switch (tipoCliente) {
            case "R":
                return REGULAR;
            case "M":
                return MEIA_ENTRADA;
            case "C":
                return CLUBE_CINEMA;
            default:
                return TIPO_CLIENTE_INVALIDO;
        }
    }

    // retorna se o cliente é meia entrada a partir de uma interação do usuário
    public static boolean ehClienteMeiaEntrada(InteracaoUsuario interacaoUsuario) {
        return interacaoUsuario.obterTipoCliente().equals(TipoClienteEnum.MEIA_ENTRADA);
    }

    // retorna se o cliente é clube cinema a partir de uma interação do usuário
    public static boolean ehClienteClubeCinema(InteracaoUsuario interacaoReservada) {
        return interacaoReservada.obterTipoCliente().equals(TipoClienteEnum.CLUBE_CINEMA);
    }

}
