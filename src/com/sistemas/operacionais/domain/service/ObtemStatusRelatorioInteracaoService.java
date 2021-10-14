package com.sistemas.operacionais.domain.service;

import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.Poltrona;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.model.enums.UltimaAcaoEnum;

// Obtém o status da interação que será exibido no relatório
public class ObtemStatusRelatorioInteracaoService {
    private final SalaCinemaQueryService queryService;

    private ObtemStatusRelatorioInteracaoService(SalaCinema salaCinema) {
        this.queryService = SalaCinemaQueryService.build(salaCinema.obterPoltronas());
    }

    public static ObtemStatusRelatorioInteracaoService build(SalaCinema salaCinema) {
        return new ObtemStatusRelatorioInteracaoService(salaCinema);
    }

    // Obtém o status da interação que será exibido no relatório
    public String obterStatus(InteracaoUsuario interacaoUsuario) {
        if(UltimaAcaoEnum.ehFluxoReservaCompleto(interacaoUsuario)) {
            Poltrona poltrona = queryService.obterPoltrona(interacaoUsuario);
            if (poltrona.obterInteracaoReservaAtual() == interacaoUsuario) {
                return interacaoUsuario.ehRealocacao() ? "Realocado("+interacaoUsuario.obterPoltronaInteracao()+")" : "Reservado";
            }
            return "Não foi possível realizar reserva";
        }
        return interacaoUsuario.obterUltimaAcao().obterStatus();
    }
}
