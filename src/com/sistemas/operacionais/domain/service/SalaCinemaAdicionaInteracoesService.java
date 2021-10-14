package com.sistemas.operacionais.domain.service;

import com.sistemas.operacionais.domain.builder.InteracaoUsuarioBuilder;
import com.sistemas.operacionais.domain.model.ConfiguracaoSalaCinema;
import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.Poltrona;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.model.enums.UltimaAcaoEnum;

public class SalaCinemaAdicionaInteracoesService {
    private final SalaCinemaQueryService queryService;
    private final SalaCinemaRealizaReservaService salaCinemaRealizaReservaService;

    public SalaCinemaAdicionaInteracoesService(SalaCinema salaCinema, ConfiguracaoSalaCinema configuracaoSalaCinema) {
        this.queryService = SalaCinemaQueryService.build(salaCinema.obterPoltronas());
        this.salaCinemaRealizaReservaService = new SalaCinemaRealizaReservaService(salaCinema, configuracaoSalaCinema);
    }

    public void adicionaInteracao(String interacao, int line) {
        InteracaoUsuario interacaoUsuario = InteracaoUsuarioBuilder.build(interacao, line);
        Poltrona poltrona = queryService.obterPoltrona(interacaoUsuario);
        poltrona.adicionaInteracao(interacaoUsuario);
        if (UltimaAcaoEnum.ehFluxoReservaCompleto(interacaoUsuario)) {
            salaCinemaRealizaReservaService.realizaReserva(poltrona, interacaoUsuario);
        }
    }
}
