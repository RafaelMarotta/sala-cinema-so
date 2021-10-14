package com.sistemas.operacionais.domain.builder;

import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.enums.TipoClienteEnum;
import com.sistemas.operacionais.domain.model.enums.UltimaAcaoEnum;

public class InteracaoUsuarioBuilder {
    private static final char DIVISOR_INTERACAO = ';';

    /* contrói interação do usuário a partir de uma linha do arquivo, 
    * separando poltrona, horário, comportamento nas fases, tipo de cliente e tempo da interação
    */
    public static InteracaoUsuario build(String interacao, int line) {
        String[] campos = interacao.split(Character.toString(DIVISOR_INTERACAO));
        String poltrona = campos[0];
        String horario = campos[1];
        UltimaAcaoEnum ultimaAcao = UltimaAcaoEnum.obterAcao(campos[2]);
        boolean tentaBuscaOutraPoltrona = campos[3].equals("T");
        TipoClienteEnum tipoCliente = TipoClienteEnum.obterTipoCliente(campos[4]);
        int tempoGasto = Integer.parseInt(campos[5]);
        return new InteracaoUsuario(line, poltrona, horario, ultimaAcao, tipoCliente, tentaBuscaOutraPoltrona, tempoGasto);
    }
}
