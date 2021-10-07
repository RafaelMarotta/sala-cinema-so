package com.sistemas.operacionais.domain;

import com.sistemas.operacionais.domain.enums.TipoClienteEnum;
import com.sistemas.operacionais.exceptions.PoltronaNaoDisponivelException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Poltrona {

    private final List<InteracaoUsuario> interacoes = new ArrayList<>();
    private int idInteracaoReserva = 0;

    public void adicionaInteracao(InteracaoUsuario interacaoUsuario) {
        interacoes.add(interacaoUsuario);
    }

    public void realizaReserva(InteracaoUsuario interacaoUsuario) throws PoltronaNaoDisponivelException {
        if (ehPossivelReservar(interacaoUsuario)) {
            idInteracaoReserva = interacaoUsuario.getId();
        } else {
            throw new PoltronaNaoDisponivelException();
        }
    }

    public InteracaoUsuario obterInteracaoReservaAtual() {
        return interacoes.get(idInteracaoReserva);
    }

    private boolean ehPossivelReservar(InteracaoUsuario interacaoUsuario) {
         if (ehPoltronaNaoReservada())
             return true;
         return ehInteracaoPrioritariaSobreReservaAtual(interacaoUsuario);
    }

    private boolean ehInteracaoPrioritariaSobreReservaAtual(InteracaoUsuario interacaoUsuario) {
        InteracaoUsuario interacaoReservada = Objects.requireNonNull(obterInteracaoReservaAtual());
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
