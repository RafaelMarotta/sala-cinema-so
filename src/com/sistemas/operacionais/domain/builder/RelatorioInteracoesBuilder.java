package com.sistemas.operacionais.domain.builder;

import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.RelatorioInteracoes;
import com.sistemas.operacionais.domain.model.RelatorioInteracoesItem;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.service.ObtemStatusRelatorioInteracaoService;

import java.util.ArrayList;
import java.util.List;

public class RelatorioInteracoesBuilder {
    private final SalaCinema salaCinema;
    private final ObtemStatusRelatorioInteracaoService statusService;

    private RelatorioInteracoesBuilder(SalaCinema salaCinema) {
        this.salaCinema = salaCinema;
        this.statusService = ObtemStatusRelatorioInteracaoService.build(salaCinema);
    }

    public static RelatorioInteracoesBuilder builder(SalaCinema salaCinema) {
        return new RelatorioInteracoesBuilder(salaCinema); // Utilitário para construção do builder (Seguindo padrão lombok)
    }

    public RelatorioInteracoes build() {
        return new RelatorioInteracoes(build(new ArrayList<>()));
    }

    // Percorre a lista de interações e converte para uma lista de itens do relatório
    private List<RelatorioInteracoesItem> build(List<RelatorioInteracoesItem> items) {
        salaCinema.obterPoltronas().values().forEach(sessao ->
                sessao.values().forEach(poltrona ->
                        poltrona.obterInteracoes().forEach(interacao ->
                                items.add(build(interacao))
                        ))
        );
        return items;
    }

    // Converte a entidade "InteracaoUsuario" para a entidade "RelatorioInteracoesItem"
    private RelatorioInteracoesItem build(InteracaoUsuario interacaoUsuario) {
        return new RelatorioInteracoesItem(
                "Cliente " + (interacaoUsuario.obterId()+1),
                interacaoUsuario.obterPoltrona(),
                interacaoUsuario.obterHorario(),
                statusService.obterStatus(interacaoUsuario)
        );
    }

}
