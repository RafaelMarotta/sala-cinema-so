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
        super.realocaReservas(); // Chama o método da classe pai que realoca os excedentes normais
        //Percorre a lista de excedentes meia entrada (Pq ultrapassou os 40% ...) e tenta realocar
        salaCinema.obterExcedentesMeiaEntrada().forEach(this::realocaExcedenteMeiaEntrada);
    }

    private void realocaExcedenteMeiaEntrada(InteracaoUsuario interacaoUsuario) {
        if (!queryService.ultrapassaPorcentagemLimiteClientesMeiaEntrada(
                interacaoUsuario.obterHorario(),
                configuracaoSalaCinema.obterQuantidadeTotalPoltronas()
        )) { // Caso não ultrapasse o limite de clientes meia entrada
            realocaReservaMeiaEntrada(interacaoUsuario); // Chama o método da classe pai responsável por realocar a reserva
        }
    }
}
