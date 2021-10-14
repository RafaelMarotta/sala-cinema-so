package com.sistemas.operacionais.domain.model;

import com.sistemas.operacionais.domain.model.enums.TipoClienteEnum;
import com.sistemas.operacionais.domain.model.enums.UltimaAcaoEnum;

/* Modela as informações de uma interação do usuário, como seu id, poltrona,
*  horário escolhido, ultima ação, tipo do cliente, se tenta buscar outra poltrona,
* tempo da interação e se é relocação
*/
public class InteracaoUsuario {
    private final int id;
    private final String poltrona;
    private final String horario;
    private final UltimaAcaoEnum ultimaAcao;
    private final TipoClienteEnum tipoCliente;
    private final boolean tentaBuscaOutraPoltrona;
    private final int tempoGasto;
    private boolean ehRealocacao;

    public InteracaoUsuario(int id, String poltrona, String horario, UltimaAcaoEnum ultimaAcao, TipoClienteEnum tipoCliente, boolean tentaBuscaOutraPoltrona, int tempoGasto) {
        this.id = id;
        this.poltrona = poltrona;
        this.horario = horario;
        this.ultimaAcao = ultimaAcao;
        this.tipoCliente = tipoCliente;
        this.tentaBuscaOutraPoltrona = tentaBuscaOutraPoltrona;
        this.tempoGasto = tempoGasto;
        this.ehRealocacao = false;
    }

    public void setaInteracaoComoRealocacao() {
        this.ehRealocacao = true;
    }

    public int obterId() {
        return id;
    }
    public String obterPoltrona() {
        return poltrona;
    }
    public String obterHorario() {
        return horario;
    }
    public UltimaAcaoEnum obterUltimaAcao() {
        return ultimaAcao;
    }
    public boolean ehTentaBuscaOutraPoltrona() {
        return tentaBuscaOutraPoltrona;
    }
    public int obterTempoGasto() {
        return tempoGasto;
    }
    public TipoClienteEnum obterTipoCliente() {
        return tipoCliente;
    }
    public boolean ehRealocacao() {
        return ehRealocacao;
    }
}
