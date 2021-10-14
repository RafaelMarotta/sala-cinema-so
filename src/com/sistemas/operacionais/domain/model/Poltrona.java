package com.sistemas.operacionais.domain.model;

import com.sistemas.operacionais.domain.model.enums.TipoClienteEnum;
import com.sistemas.operacionais.exceptions.PoltronaNaoDisponivelException;

import java.util.ArrayList;
import java.util.List;

// Modela as informações de uma poltrona, como suas interações e o ID da interação que a reservou
public class Poltrona {
    private final List<InteracaoUsuario> interacoes = new ArrayList<>();
    private int idInteracaoReserva = 0;

    public void adicionaInteracao(InteracaoUsuario interacaoUsuario) {
        interacoes.add(interacaoUsuario);
    }
    
    // Realiza a reserva caso esteja disponível
    public void realizaReserva(InteracaoUsuario interacaoUsuario) throws PoltronaNaoDisponivelException {
        if (ehPossivelReservar(interacaoUsuario)) {
            idInteracaoReserva = interacaoUsuario.obterId();
        } else {
            throw new PoltronaNaoDisponivelException();
        }
    }

    // retorna a interação de reserva, caso exista
    public InteracaoUsuario obterInteracaoReservaAtual() {
        return interacoes.stream().filter(e -> e.obterId() == idInteracaoReserva).findFirst().orElse(null);
    }

    public List<InteracaoUsuario> obterInteracoes() {
        return interacoes;
    }

    // retorna se a poltrona é possivel ser reservada
    private boolean ehPossivelReservar(InteracaoUsuario interacaoUsuario) {
         if (ehPoltronaNaoReservada())
             return true;
         return ehInteracaoPrioritariaSobreReservaAtual(interacaoUsuario);
    }

    // retorna se uma interação tem maior prioridade em relação a reserva atual da poltrona
    private boolean ehInteracaoPrioritariaSobreReservaAtual(InteracaoUsuario interacaoUsuario) {
        InteracaoUsuario interacaoReservada = obterInteracaoReservaAtual();
        if (TipoClienteEnum.ehClienteClubeCinema(interacaoReservada))
            return false;
        if (TipoClienteEnum.ehClienteClubeCinema(interacaoUsuario))
            return true;
        if (TipoClienteEnum.ehClienteMeiaEntrada(interacaoReservada))
            return false;
        return TipoClienteEnum.ehClienteMeiaEntrada(interacaoUsuario);
    }

    private boolean ehPoltronaNaoReservada() {
        return obterInteracaoReservaAtual() == null;
    }
}
