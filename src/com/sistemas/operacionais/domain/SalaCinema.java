package com.sistemas.operacionais.domain;

import com.sistemas.operacionais.domain.enums.TipoClienteEnum;
import com.sistemas.operacionais.domain.enums.UltimaAcaoEnum;
import com.sistemas.operacionais.exceptions.PoltronaNaoDisponivelException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalaCinema {

    private final Map<String, Map<Integer, Poltrona>> poltronas;
    private final ConfiguracaoSalaCinema configuracaoSalaCinema;
    private static final char[] ALFABETO = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

    public SalaCinema(ConfiguracaoSalaCinema configuracaoSalaCinema) {
        this.poltronas = new HashMap<>();
        this.configuracaoSalaCinema = configuracaoSalaCinema;
    }

    public void adicionaInteracao(String interacao, int line) {
        InteracaoUsuario interacaoUsuario = new InteracaoUsuario(interacao, line);
        obterPoltrona(interacaoUsuario).adicionaInteracao(interacaoUsuario);
    }

    private void realizaReserva(Poltrona poltrona, InteracaoUsuario interacaoUsuario) {
        if (UltimaAcaoEnum.ehFluxoReservaCompleto(interacaoUsuario)) {
            if (TipoClienteEnum.ehClienteMeiaEntrada(interacaoUsuario)) {
                realizaReservaMeiaEntrada(poltrona, interacaoUsuario);
            }
        }
    }

    private void realizaReservaMeiaEntrada(Poltrona poltrona, InteracaoUsuario interacaoUsuario) {
        if (ultrapassaPorcentagemLimiteClientesMeiaEntrada(interacaoUsuario.getHorario())) {
            // Adiciona lista espera meia entrada
        } else {
            try {
                poltrona.realizaReserva(interacaoUsuario);
            } catch (PoltronaNaoDisponivelException e) {
                if (interacaoUsuario.ehTentaBuscaOutraPoltrona()) {
                    // Adiciona lista excedentes
                }
            }
        }
    }

    private boolean ultrapassaPorcentagemLimiteClientesMeiaEntrada(String sessao) {
        int quantidadeClientes = obterPoltronasReservadasPorTipoClienteESessao(sessao, TipoClienteEnum.MEIA_ENTRADA).size();
        double quantidadePoltronas = configuracaoSalaCinema.getQuantidadeTotalPoltronas().doubleValue();
        double porcentagem = ((double) quantidadeClientes +1/quantidadePoltronas)*100;
        return porcentagem > 40;
    }

    private List<Poltrona> obterPoltronasReservadasPorTipoClienteESessao(String sessao, TipoClienteEnum tipoCliente) {
        return poltronas.get(sessao).values().stream().filter(
                e -> e.obterInteracaoReservaAtual().getTipoCliente().equals(tipoCliente)
                ).collect(Collectors.toList());
    }

    private Poltrona obterPoltrona(InteracaoUsuario interacaoUsuario) {
        char fila = interacaoUsuario.getPoltrona().toCharArray()[0];
        int posicaoFila = Arrays.binarySearch(ALFABETO, fila);
        int posicaoCadeira = Integer.parseInt(interacaoUsuario.getPoltrona().substring(1));
        int posicao = posicaoFila == 0 ? posicaoCadeira : (posicaoFila+1) * 10 * posicaoCadeira;
        Map<Integer, Poltrona> sessao = poltronas.get(interacaoUsuario.getHorario());
        Poltrona poltrona = sessao.get(posicao);
        if (poltrona == null) {
            sessao.put(posicao, new Poltrona());
        }
        return poltrona;
    }

}
