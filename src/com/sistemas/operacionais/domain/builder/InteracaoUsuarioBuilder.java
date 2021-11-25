package com.sistemas.operacionais.domain.builder;

import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.enums.TipoClienteEnum;
import com.sistemas.operacionais.domain.model.enums.UltimaAcaoEnum;

public class InteracaoUsuarioBuilder {
    private static final char DIVISOR_INTERACAO = ';';

    // Converte uma linha do arquivo de interação para a entidade de modelo "InteracaoUsuario"
    public static InteracaoUsuario build(String interacao, int line, int idPontoVenda) {
        String[] campos = interacao.split(Character.toString(DIVISOR_INTERACAO));
        String poltrona = campos[0];
        String horario = campos[1];
        UltimaAcaoEnum ultimaAcao = UltimaAcaoEnum.obterAcao(campos[2]);
        boolean tentaBuscaOutraPoltrona = campos[3].equals("T");
        TipoClienteEnum tipoCliente = TipoClienteEnum.obterTipoCliente(campos[4]);
        int tempoGasto = Integer.parseInt(campos[5]);
        int tempoGastoEntradaProximoCliente = Integer.parseInt(campos[6]);
        return new InteracaoUsuario(line, idPontoVenda, poltrona, horario, ultimaAcao, tipoCliente, tentaBuscaOutraPoltrona, tempoGasto, tempoGastoEntradaProximoCliente);
    }
}
