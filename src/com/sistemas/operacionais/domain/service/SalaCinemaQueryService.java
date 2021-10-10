package com.sistemas.operacionais.domain.service;

import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.Poltrona;
import com.sistemas.operacionais.domain.model.enums.TipoClienteEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalaCinemaQueryService {
    private static final char[] ALFABETO = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    private final Map<String, Map<Integer, Poltrona>> poltronas;

    private SalaCinemaQueryService(Map<String, Map<Integer, Poltrona>> poltronas) {
        this.poltronas = poltronas;
    }

    public static SalaCinemaQueryService build(Map<String, Map<Integer, Poltrona>> poltronas) {
        return new SalaCinemaQueryService(poltronas);
    }

    public Poltrona obterPoltrona(InteracaoUsuario interacaoUsuario) {
        int posicao = obterPosicaoPoltrona(interacaoUsuario.obterPoltrona());
        Map<Integer, Poltrona> sessao = poltronas.get(interacaoUsuario.obterHorario());
        return sessao.get(posicao);
    }

    private int obterPosicaoPoltrona(String poltrona) {
        char fila = poltrona.toCharArray()[0];
        int posicaoFila = Arrays.binarySearch(ALFABETO, fila)+1;
        int posicaoCadeira = Integer.parseInt(poltrona.substring(1));
        return posicaoFila*posicaoCadeira;
    }

    public List<Poltrona> obterPoltronasReservadasPorTipoClienteESessao(String sessao, TipoClienteEnum tipoCliente) {
        return poltronas.get(sessao).values().stream().filter(
                e -> ehTipoCliente(e, tipoCliente)
        ).collect(Collectors.toList());
    }

    private boolean ehTipoCliente(Poltrona poltrona, TipoClienteEnum tipoCliente) {
        if (poltrona.obterInteracaoReservaAtual() == null) {
            return false;
        }
        return poltrona.obterInteracaoReservaAtual().obterTipoCliente().equals(tipoCliente);
    }
}
