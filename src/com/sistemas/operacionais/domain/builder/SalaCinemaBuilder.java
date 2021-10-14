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
    private static final char[] ALFABETO = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    private final Map<String, Map<String, Poltrona>> poltronas;
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

    // Cria lista de poltronas vazias de acordo com o modelo ConfiguracaoSalaCinema
    private Map<String, Poltrona> obterPoltronas() {
        Map<String, Poltrona> poltronas = new HashMap<>();
        for (int i = 0; i < configuracaoSalaCinema.obterQuantidadeFileiras(); i++) {
           for (int i1 = 1; i1 <= configuracaoSalaCinema.obterQuantidadeCadeirasFileira(); i1++) {
               String id = String.format("%c%02d", ALFABETO[i], i1);
               poltronas.put(id, new Poltrona(id));
           }
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
