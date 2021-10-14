package com.sistemas.operacionais.domain.model;

import com.sistemas.operacionais.domain.model.enums.TipoClienteEnum;
import com.sistemas.operacionais.domain.service.interfaces.IAdicionaFilaExcedentes;
import com.sistemas.operacionais.exceptions.PoltronaNaoDisponivelException;

import java.util.ArrayList;
import java.util.List;

// Modelo que representa uma poltrona
public class Poltrona {
    private final String id;
    private final List<InteracaoUsuario> interacoes = new ArrayList<>();
    private int idInteracaoReserva = -1;

    public Poltrona(String id) {
        this.id = id;
    }

    // Adiciona uma interação na lista de interações realizadas na poltrona
    public void adicionaInteracao(InteracaoUsuario interacaoUsuario) {
        interacoes.add(interacaoUsuario);
    }

    // Tenta Realizar a reserva
    public void realizaReserva(InteracaoUsuario interacaoUsuario, IAdicionaFilaExcedentes adicionaFilaExcedentes) throws PoltronaNaoDisponivelException {
        if (ehPossivelReservar(interacaoUsuario)) { // Valida se é possível registrar a reserva
            // Se a poltrona está reserva e o cliente é marcado como buscar outra poltrona
            if(!ehPoltronaNaoReservada() && obterInteracaoReservaAtual().ehTentaBuscaOutraPoltrona()) {
                adicionaFilaExcedentes.adicionaExcedente(obterInteracaoReservaAtual()); // Adiciona na fila de excedentes
            }
            idInteracaoReserva = interacaoUsuario.obterId(); // Seta o id da reserva atual para o id da interação
        } else {
            throw new PoltronaNaoDisponivelException(); // Lança a exceção para ser tratada nos métodos que chamaram
        }
    }

    // Obtém a interação que representa a reserva nesse momento
    public InteracaoUsuario obterInteracaoReservaAtual() {
        return interacoes.stream().filter(e -> e.obterId() == idInteracaoReserva).findFirst().orElse(null);
    }

    // Obtém a lista de interações com a Poltrona
    public List<InteracaoUsuario> obterInteracoes() {
        return interacoes;
    }

    // Valida se é possível reservar a interação
    private boolean ehPossivelReservar(InteracaoUsuario interacaoUsuario) {
         if (ehPoltronaNaoReservada()) // Se a poltrona não está reservada
             return true; // Retorna que é possível
         return ehInteracaoPrioritariaSobreReservaAtual(interacaoUsuario); // Valida se a interação tem prioridade sobre a reserva atual
    }

    private boolean ehInteracaoPrioritariaSobreReservaAtual(InteracaoUsuario interacaoUsuario) {
        InteracaoUsuario interacaoReservada = obterInteracaoReservaAtual(); // Obtém interação da reserva atual
        if (TipoClienteEnum.ehClienteClubeCinema(interacaoReservada)) // Se a interação atual é clube cinema
            return false; // Retorna não prioritário
        if (TipoClienteEnum.ehClienteClubeCinema(interacaoUsuario)) // Se a interação que quer reservar é clube cinema
            return true; // Retorna prioritário
        if (TipoClienteEnum.ehClienteMeiaEntrada(interacaoReservada)) // Se a interação da reserva atual é meia entrada
            return false; // Retorna não prioritário
        return TipoClienteEnum.ehClienteMeiaEntrada(interacaoUsuario); // Se a interação que quer reservar é meia entrada retorna prioritário
    }

    private boolean ehPoltronaNaoReservada() {
        return obterInteracaoReservaAtual() == null;
    } // Valida se a poltrona ainda não foi reservada por ninguém

    public String obterId() {
        return this.id;
    } // Obtém o Id da poltrona
}
