package com.sistemas.operacionais.domain.model;

import com.sistemas.operacionais.domain.model.enums.TipoClienteEnum;
import com.sistemas.operacionais.domain.model.enums.UltimaAcaoEnum;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// Modelo que armazena a interação de usuário
public class InteracaoUsuario {
    private final int id;
    private final int idPontoVenda;
    private String poltrona;
    private final String poltronaInteracao;
    private final String horario;
    private final UltimaAcaoEnum ultimaAcao;
    private final TipoClienteEnum tipoCliente;
    private final boolean tentaBuscaOutraPoltrona;
    private final int tempoGasto;
    private final int tempoEntradaProximoCliente;
    private boolean ehRealocacao;

    public InteracaoUsuario(int id, int idPontoVenda, String poltrona, String horario, UltimaAcaoEnum ultimaAcao, TipoClienteEnum tipoCliente, boolean tentaBuscaOutraPoltrona, int tempoGasto, int tempoEntradaProximoCliente) {
        this.id = id;
        this.idPontoVenda = idPontoVenda;
        this.poltrona = poltrona;
        this.poltronaInteracao = poltrona;
        this.horario = horario;
        this.ultimaAcao = ultimaAcao;
        this.tipoCliente = tipoCliente;
        this.tentaBuscaOutraPoltrona = tentaBuscaOutraPoltrona;
        this.tempoGasto = tempoGasto;
        this.tempoEntradaProximoCliente = tempoEntradaProximoCliente;
        this.ehRealocacao = false;
    }

    public void realocaPoltrona(String poltrona) {
        this.ehRealocacao = true;
        this.poltrona = poltrona;
    }

    public int obterId() {
        return id;
    }

    public int obterIdPontoVenda() {
        return idPontoVenda;
    }

    public String obterPoltrona() {
        return poltrona;
    }
    public String obterPoltronaInteracao() { return  poltronaInteracao; }
    public String obterHorario() {
        return horario;
    }
    public LocalTime obterHorarioLocalTime() {
        return LocalTime.parse(horario, DateTimeFormatter.ofPattern("HH:mm"));
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
