package com.sistemas.operacionais.domain.model;

import java.util.List;
import java.util.Map;

public class SalaCinema {
    private final Map<String, Map<Integer, Poltrona>> poltronas;
    private final List<InteracaoUsuario> excedentes;
    private final List<InteracaoUsuario> excedentesMeiaEntrada;

    public SalaCinema(Map<String, Map<Integer, Poltrona>> poltronas, List<InteracaoUsuario> excedentes, List<InteracaoUsuario> excedentesMeiaEntrada) {
        this.poltronas = poltronas;
        this.excedentes = excedentes;
        this.excedentesMeiaEntrada = excedentesMeiaEntrada;
    }

    public Map<String, Map<Integer, Poltrona>> obterPoltronas() {
        return poltronas;
    }

    public List<InteracaoUsuario> obterExcedentes() {
        return excedentes;
    }

    public List<InteracaoUsuario> obterExcedentesMeiaEntrada() {
        return excedentesMeiaEntrada;
    }
}
