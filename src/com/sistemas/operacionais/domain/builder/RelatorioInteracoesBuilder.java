package com.sistemas.operacionais.domain.builder;

import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.RelatorioInteracoes;
import com.sistemas.operacionais.domain.model.RelatorioInteracoesItem;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.service.ObterStatusRelatorioInteracaoService;

import java.util.ArrayList;
import java.util.List;

public class RelatorioInteracoesBuilder {
    private final SalaCinema salaCinema;
    private final ObterStatusRelatorioInteracaoService statusService;

    private RelatorioInteracoesBuilder(SalaCinema salaCinema) {
        this.salaCinema = salaCinema;
        this.statusService = ObterStatusRelatorioInteracaoService.build(salaCinema);
    }

    public static RelatorioInteracoesBuilder builder(SalaCinema salaCinema) {
        return new RelatorioInteracoesBuilder(salaCinema);
    }

    public RelatorioInteracoes build() {
        return new RelatorioInteracoes(build(new ArrayList<>()));
    }

    private List<RelatorioInteracoesItem> build(List<RelatorioInteracoesItem> items) {
        salaCinema.obterPoltronas().values().forEach(sessao ->
                sessao.values().forEach(poltrona ->
                        poltrona.obterInteracoes().forEach(interacao ->
                                items.add(build(interacao))
                        ))
        );
        return items;
    }

    private RelatorioInteracoesItem build(InteracaoUsuario interacaoUsuario) {
        return new RelatorioInteracoesItem(
                "Cliente " + (interacaoUsuario.obterId()+1),
                interacaoUsuario.obterPoltrona(),
                interacaoUsuario.obterHorario(),
                statusService.obterStatus(interacaoUsuario)
        );
    }

}
