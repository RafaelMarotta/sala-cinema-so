package com.sistemas.operacionais.domain.service;

import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.Poltrona;
import com.sistemas.operacionais.domain.model.enums.TipoClienteEnum;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalaCinemaQueryService {
    private final Map<String, Map<String, Poltrona>> poltronas;

    private SalaCinemaQueryService(Map<String, Map<String, Poltrona>> poltronas) {
        this.poltronas = poltronas;
    }

    public static SalaCinemaQueryService build(Map<String, Map<String, Poltrona>> poltronas) {
        return new SalaCinemaQueryService(poltronas);
    }

    public Poltrona obterPoltrona(InteracaoUsuario interacaoUsuario) {
        Map<String, Poltrona> sessao = poltronas.get(interacaoUsuario.obterHorario());
        return sessao.get(interacaoUsuario.obterPoltrona());
    }

    public List<Poltrona> obterPoltronasReservadasPorTipoClienteESessao(String sessao, TipoClienteEnum tipoCliente) {
        return poltronas.get(sessao).values().stream().filter(
                e -> ehTipoCliente(e, tipoCliente)
        ).collect(Collectors.toList());
    }

    private boolean ehTipoCliente(Poltrona poltrona, TipoClienteEnum tipoCliente) {
        if (poltrona.obterInteracaoReservaAtual() == null) {
            return tipoCliente == null;
        }
        return poltrona.obterInteracaoReservaAtual().obterTipoCliente().equals(tipoCliente);
    }

    public boolean ultrapassaPorcentagemLimiteClientesMeiaEntrada(String sessao, double quantidadePoltronas) {
        int quantidadeClientes = obterPoltronasReservadasPorTipoClienteESessao(sessao, TipoClienteEnum.MEIA_ENTRADA).size();
        double porcentagem = ((double) quantidadeClientes +1/quantidadePoltronas)*100;
        return porcentagem > 40;
    }
}
