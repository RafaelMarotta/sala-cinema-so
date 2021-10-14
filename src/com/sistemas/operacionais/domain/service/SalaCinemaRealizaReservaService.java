package com.sistemas.operacionais.domain.service;

import com.sistemas.operacionais.domain.model.ConfiguracaoSalaCinema;
import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.Poltrona;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.model.enums.TipoClienteEnum;
import com.sistemas.operacionais.domain.service.interfaces.IAdicionaFilaExcedentes;
import com.sistemas.operacionais.exceptions.PoltronaNaoDisponivelException;

// Lógica de realização de reserva
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
        if (TipoClienteEnum.ehClienteMeiaEntrada(interacaoUsuario)) {  // Se o cliente for meia entrada
            realizaReservaMeiaEntrada(poltrona, interacaoUsuario); // Método que trata a reserva de clientes meia entrada
        } else {
            realizaReservaComTratamentoExcedentes(poltrona, interacaoUsuario); // Método que trata a reserva dos demais clientes
        }
    }

    private void realizaReservaComTratamentoExcedentes(Poltrona poltrona, InteracaoUsuario interacaoUsuario) {
        try {
            poltrona.realizaReserva(interacaoUsuario, this); // Tentar realizar reserva
        } catch (PoltronaNaoDisponivelException e) { // Lançada caso a poltrona esteja ocupada por alguém com uma prioridade maior
            if (interacaoUsuario.ehTentaBuscaOutraPoltrona()) {  // Se a Interacao está marcada como buscar outra poltrona
                adicionaExcedente(interacaoUsuario); // Adiciona na lista de excedentes
            }
        }
    }

    private void realizaReservaMeiaEntrada(Poltrona poltrona, InteracaoUsuario interacaoUsuario) {
        if (queryService.ultrapassaPorcentagemLimiteClientesMeiaEntrada(interacaoUsuario.obterHorario(),
                configuracaoSalaCinema.obterQuantidadeTotalPoltronas())) { // Ultrapasar o limite de clientes meia entrada (40%)
            salaCinema.obterExcedentesMeiaEntrada().add(interacaoUsuario); // Adiciona na lista de excedentes clientes meia entrada
        } else {
            realizaReservaComTratamentoExcedentes(poltrona, interacaoUsuario); // Chama o fluxo normal de realização de reservas
        }
    }

    @Override
    public void adicionaExcedente(InteracaoUsuario interacaoUsuario) {
        salaCinema.obterExcedentes().add(interacaoUsuario); //Implementa a interface IAdicionaFilaExcedentes
    }
}
