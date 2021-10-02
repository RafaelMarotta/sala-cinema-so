package com.sistemas.operacionais;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SalaCinema {

    private Map<String, List<Poltrona>> poltronas;
    private static final char[] ALFABETO = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

    public void adicionaInteracao(String interacao, int line) {
        InteracaoUsuario interacaoUsuario = new InteracaoUsuario(interacao, line);
        obterPoltrona(interacaoUsuario).addInteracao(interacaoUsuario);
    }

    private Poltrona obterPoltrona(InteracaoUsuario interacaoUsuario) {
        char fila = interacaoUsuario.getPoltrona().toCharArray()[0];
        int posicaoFila = Arrays.binarySearch(ALFABETO, fila);
        int posicaoCadeira = Integer.parseInt(interacaoUsuario.getPoltrona().substring(1));
        int posicao = posicaoFila == 0 ? posicaoCadeira : (posicaoFila+1)*10*posicaoCadeira;
        return poltronas.get(interacaoUsuario.getHorario()).get(posicao);
    }

}
