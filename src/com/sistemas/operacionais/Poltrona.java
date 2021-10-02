package com.sistemas.operacionais;

import java.util.ArrayList;
import java.util.List;

public class Poltrona {

    private final List<InteracaoUsuario> interacoes = new ArrayList<>();

    public void addInteracao(InteracaoUsuario interacaoUsuario) {
        interacoes.add(interacaoUsuario);
    }

}
