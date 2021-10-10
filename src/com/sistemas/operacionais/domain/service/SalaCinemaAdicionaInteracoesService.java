package com.sistemas.operacionais.domain.service;

import com.sistemas.operacionais.domain.builder.InteracaoUsuarioBuilder;
import com.sistemas.operacionais.domain.model.ConfiguracaoSalaCinema;
import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.Poltrona;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.model.enums.TipoClienteEnum;
import com.sistemas.operacionais.domain.model.enums.UltimaAcaoEnum;
import com.sistemas.operacionais.exceptions.PoltronaNaoDisponivelException;

public class SalaCinemaAdicionaInteracoesService {
    private final SalaCinema salaCinema;
    private final SalaCinemaQueryService queryService;
    private final ConfiguracaoSalaCinema configuracaoSalaCinema;

    public SalaCinemaAdicionaInteracoesService(SalaCinema salaCinema, ConfiguracaoSalaCinema configuracaoSalaCinema) {
        this.salaCinema = salaCinema;
        this.queryService = SalaCinemaQueryService.build(salaCinema.obterPoltronas());
        this.configuracaoSalaCinema = configuracaoSalaCinema;
    }

    public void adicionaInteracao(String interacao, int line) {
        InteracaoUsuario interacaoUsuario = InteracaoUsuarioBuilder.build(interacao, line);
        Poltrona poltrona = queryService.obterPoltrona(interacaoUsuario);
        poltrona.adicionaInteracao(interacaoUsuario);
        if (UltimaAcaoEnum.ehFluxoReservaCompleto(interacaoUsuario)) {
            realizaReserva(poltrona, interacaoUsuario);
        }
    }

    private void realizaReserva(Poltrona poltrona, InteracaoUsuario interacaoUsuario) {
        if (TipoClienteEnum.ehClienteMeiaEntrada(interacaoUsuario)) {
            realizaReservaMeiaEntrada(poltrona, interacaoUsuario);
        } else {
            realizaReservaComTratamentoExcedentes(poltrona, interacaoUsuario);
        }
    }

    private void realizaReservaComTratamentoExcedentes(Poltrona poltrona, InteracaoUsuario interacaoUsuario) {
        try {
            poltrona.realizaReserva(interacaoUsuario);
        } catch (PoltronaNaoDisponivelException e) {
            if (interacaoUsuario.ehTentaBuscaOutraPoltrona()) {
                salaCinema.obterExcedentes().add(interacaoUsuario);
            }
        }
    }

    private void realizaReservaMeiaEntrada(Poltrona poltrona, InteracaoUsuario interacaoUsuario) {
        if (ultrapassaPorcentagemLimiteClientesMeiaEntrada(interacaoUsuario.obterHorario())) {
            salaCinema.obterExcedentesMeiaEntrada().add(interacaoUsuario);
        } else {
            realizaReservaComTratamentoExcedentes(poltrona, interacaoUsuario);
        }
    }

    private boolean ultrapassaPorcentagemLimiteClientesMeiaEntrada(String sessao) {
        int quantidadeClientes = queryService.obterPoltronasReservadasPorTipoClienteESessao(sessao, TipoClienteEnum.MEIA_ENTRADA).size();
        double quantidadePoltronas = configuracaoSalaCinema.obterQuantidadeTotalPoltronas();
        double porcentagem = ((double) quantidadeClientes +1/quantidadePoltronas)*100;
        return porcentagem > 40;
    }
}
