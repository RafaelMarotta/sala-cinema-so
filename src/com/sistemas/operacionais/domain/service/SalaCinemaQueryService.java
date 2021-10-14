package com.sistemas.operacionais.domain.service;

import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.Poltrona;
import com.sistemas.operacionais.domain.model.enums.TipoClienteEnum;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
// Classe responsável por encapsular lógicas de buscas complexas
// no map que armazenas a lista de poltronas por sessões
* */

public class SalaCinemaQueryService {
    private final Map<String, Map<String, Poltrona>> poltronas;
    // Map<Horário, Map<Nome Poltrona, Objeto de modelo poltrona>>

    private SalaCinemaQueryService(Map<String, Map<String, Poltrona>> poltronas) {
        this.poltronas = poltronas;
    }

    public static SalaCinemaQueryService build(Map<String, Map<String, Poltrona>> poltronas) {
        return new SalaCinemaQueryService(poltronas);
    }

    // Obtém a poltrona relativa a uma interação do usuário
    public Poltrona obterPoltrona(InteracaoUsuario interacaoUsuario) {
        Map<String, Poltrona> sessao = poltronas.get(interacaoUsuario.obterHorario());
        return sessao.get(interacaoUsuario.obterPoltrona());
    }

    // Obtém poltronas reservadas de acordo com o tipo do usuário e sessão
    public List<Poltrona> obterPoltronasReservadasPorTipoClienteESessao(String sessao, TipoClienteEnum tipoCliente) {
        return poltronas.get(sessao).values().stream().filter(
                e -> ehTipoCliente(e, tipoCliente)
        ).collect(Collectors.toList());
    }

    // Valida se o tipo do cliente que está prevalecendo na reserva é igual ao passado para o método
    private boolean ehTipoCliente(Poltrona poltrona, TipoClienteEnum tipoCliente) {
        if (poltrona.obterInteracaoReservaAtual() == null) {
            return tipoCliente == null;
        }
        return poltrona.obterInteracaoReservaAtual().obterTipoCliente().equals(tipoCliente);
    }

    // Valida se a sala ultrapassou a porcentagem limite de clientes meia entrada
    public boolean ultrapassaPorcentagemLimiteClientesMeiaEntrada(String sessao, double quantidadePoltronas) {
        int quantidadeClientes = obterPoltronasReservadasPorTipoClienteESessao(sessao, TipoClienteEnum.MEIA_ENTRADA).size();
        double porcentagem = ((double) (quantidadeClientes+1)*100)/quantidadePoltronas;
        return porcentagem > 40;
    }
}
