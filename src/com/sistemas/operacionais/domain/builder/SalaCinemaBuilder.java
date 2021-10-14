package com.sistemas.operacionais.domain.builder;

import com.sistemas.operacionais.domain.model.ConfiguracaoSalaCinema;
import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.Poltrona;
import com.sistemas.operacionais.domain.model.SalaCinema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalaCinemaBuilder {
    private final Map<String, Map<Integer, Poltrona>> poltronas;
    private final ConfiguracaoSalaCinema configuracaoSalaCinema;
    private final List<InteracaoUsuario> excedentes;
    private final List<InteracaoUsuario> excedentesMeiaEntrada;

    private SalaCinemaBuilder(ConfiguracaoSalaCinema configuracaoSalaCinema) {
        this.poltronas = new HashMap<>();
        this.excedentes = new ArrayList<>();
        this.excedentesMeiaEntrada = new ArrayList<>();
        this.configuracaoSalaCinema = configuracaoSalaCinema;
        configuracaoSalaCinema.obterSessoes().forEach(e -> poltronas.put(e, obterPoltronas()));
    }

    // mapeia poltronas associado-as a um index
    private Map<Integer, Poltrona> obterPoltronas() {
        Map<Integer, Poltrona> poltronas = new HashMap<>();
        for (int i = 1; i <= configuracaoSalaCinema.obterQuantidadeTotalPoltronas(); i++) {
            poltronas.put(i, new Poltrona());
        }
        return poltronas;
    }

    public static SalaCinemaBuilder builder(ConfiguracaoSalaCinema configuracaoSalaCinema) {
        return new SalaCinemaBuilder(configuracaoSalaCinema);
    }

    public SalaCinema build() {
        return new SalaCinema(poltronas, excedentes, excedentesMeiaEntrada);
    }

}
