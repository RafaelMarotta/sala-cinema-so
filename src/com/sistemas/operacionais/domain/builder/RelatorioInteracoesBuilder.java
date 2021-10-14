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

    // Constói um novo builder relátorio de interações a partir de uma sala de cinema
    public static RelatorioInteracoesBuilder builder(SalaCinema salaCinema) {
        return new RelatorioInteracoesBuilder(salaCinema);
    }

    // Constói um novo relátorio de interações
    public RelatorioInteracoes build() {
        return new RelatorioInteracoes(build(new ArrayList<>()));
    }

    // contrói a lista de relatórios de interações da sala de cinema
    private List<RelatorioInteracoesItem> build(List<RelatorioInteracoesItem> items) {
        salaCinema.obterPoltronas().values().forEach(sessao ->
                sessao.values().forEach(poltrona ->
                        poltrona.obterInteracoes().forEach(interacao ->
                                items.add(build(interacao))
                        ))
        );
        return items;
    }

    /* contrói um item de relatório de interações a partir de uma interação do usuário,
    * definindo o cliente, pontrona, horário e status
    */
    private RelatorioInteracoesItem build(InteracaoUsuario interacaoUsuario) {
        return new RelatorioInteracoesItem(
                "Cliente " + (interacaoUsuario.obterId()+1),
                interacaoUsuario.obterPoltrona(),
                interacaoUsuario.obterHorario(),
                statusService.obterStatus(interacaoUsuario)
        );
    }

}
