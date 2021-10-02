package com.sistemas.operacionais;

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

}
