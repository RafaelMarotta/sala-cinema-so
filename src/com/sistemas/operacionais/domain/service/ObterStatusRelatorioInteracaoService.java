package com.sistemas.operacionais.domain.service;

import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.Poltrona;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.model.enums.UltimaAcaoEnum;

public class ObterStatusRelatorioInteracaoService {
    private final SalaCinemaQueryService queryService;

    private ObterStatusRelatorioInteracaoService(SalaCinema salaCinema) {
        this.queryService = SalaCinemaQueryService.build(salaCinema.obterPoltronas());
    }

    public static ObterStatusRelatorioInteracaoService build(SalaCinema salaCinema) {
        return new ObterStatusRelatorioInteracaoService(salaCinema);
    }

    public String obterStatus(InteracaoUsuario interacaoUsuario) {
        if(UltimaAcaoEnum.ehFluxoReservaCompleto(interacaoUsuario)) {
            Poltrona poltrona = queryService.obterPoltrona(interacaoUsuario);
            if (poltrona.obterInteracaoReservaAtual().equals(interacaoUsuario)) {
                return interacaoUsuario.ehRealocacao() ? "Realocado("+interacaoUsuario.obterPoltronaInteracao()+")" : "Reservado";
            }
            return "Não foi possível realizar reserva";
        }
        return interacaoUsuario.obterUltimaAcao().obterStatus();
    }
}
