package com.sistemas.operacionais.domain.service;

import com.sistemas.operacionais.domain.model.ConfiguracaoSalaCinema;
import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.SalaCinema;

public class SalaCinemaRealocaReservaMeiaEntradaService extends SalaCinemaRealocaReservaService {
    public SalaCinemaRealocaReservaMeiaEntradaService(SalaCinema salaCinema, ConfiguracaoSalaCinema configuracaoSalaCinema) {
        super(salaCinema, configuracaoSalaCinema);
    }

    @Override
    public void realocaReservas() {
        super.realocaReservas();
        salaCinema.obterExcedentesMeiaEntrada().forEach(this::realocaExcedenteMeiaEntrada);
    }

    private void realocaExcedenteMeiaEntrada(InteracaoUsuario interacaoUsuario) {
        if (queryService.ultrapassaPorcentagemLimiteClientesMeiaEntrada(
                interacaoUsuario.obterHorario(),
                configuracaoSalaCinema.obterQuantidadeTotalPoltronas()
        )) {
            realocaReservaMeiaEntrada(interacaoUsuario);
        }
    }
}
