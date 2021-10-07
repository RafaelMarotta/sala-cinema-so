package com.sistemas.operacionais.domain.enums;

import com.sistemas.operacionais.domain.InteracaoUsuario;

public enum TipoClienteEnum {
    REGULAR,
    MEIA_ENTRADA,
    CLUBE_CINEMA,
    TIPO_CLIENTE_INVALIDO;

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

    public static boolean ehClienteMeiaEntrada(InteracaoUsuario interacaoUsuario) {
        return interacaoUsuario.getTipoCliente().equals(TipoClienteEnum.MEIA_ENTRADA);
    }

    public static boolean ehClienteClubeCinema(InteracaoUsuario interacaoReservada) {
        return interacaoReservada.getTipoCliente().equals(TipoClienteEnum.CLUBE_CINEMA);
    }

}
