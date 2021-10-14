package com.sistemas.operacionais.domain.service;

import com.sistemas.operacionais.domain.model.ConfiguracaoSalaCinema;
import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.Poltrona;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.model.enums.TipoClienteEnum;
import com.sistemas.operacionais.exceptions.PoltronaNaoDisponivelException;

public class SalaCinemaRealizaReservaService implements IAdicionaFilaExcedentes {
    private final SalaCinema salaCinema;
    private final SalaCinemaQueryService queryService;
    private final ConfiguracaoSalaCinema configuracaoSalaCinema;

    public SalaCinemaRealizaReservaService(SalaCinema salaCinema, ConfiguracaoSalaCinema configuracaoSalaCinema) {
        this.salaCinema = salaCinema;
        this.queryService = SalaCinemaQueryService.build(salaCinema.obterPoltronas());
        this.configuracaoSalaCinema = configuracaoSalaCinema;
    }

    public void realizaReserva(Poltrona poltrona, InteracaoUsuario interacaoUsuario) {
        if (TipoClienteEnum.ehClienteMeiaEntrada(interacaoUsuario)) {
            realizaReservaMeiaEntrada(poltrona, interacaoUsuario);
        } else {
            realizaReservaComTratamentoExcedentes(poltrona, interacaoUsuario);
        }
    }

    private void realizaReservaComTratamentoExcedentes(Poltrona poltrona, InteracaoUsuario interacaoUsuario) {
        try {
            poltrona.realizaReserva(interacaoUsuario, this);
        } catch (PoltronaNaoDisponivelException e) {
            if (interacaoUsuario.ehTentaBuscaOutraPoltrona()) {
                adicionaExcedente(interacaoUsuario);
            }
        }
    }

    private void realizaReservaMeiaEntrada(Poltrona poltrona, InteracaoUsuario interacaoUsuario) {
        if (queryService.ultrapassaPorcentagemLimiteClientesMeiaEntrada(interacaoUsuario.obterHorario(), configuracaoSalaCinema.obterQuantidadeTotalPoltronas())) {
            salaCinema.obterExcedentesMeiaEntrada().add(interacaoUsuario);
        } else {
            realizaReservaComTratamentoExcedentes(poltrona, interacaoUsuario);
        }
    }

    @Override
    public void adicionaExcedente(InteracaoUsuario interacaoUsuario) {
        salaCinema.obterExcedentes().add(interacaoUsuario);
    }
}
