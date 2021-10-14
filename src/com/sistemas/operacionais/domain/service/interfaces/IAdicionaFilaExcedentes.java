package com.sistemas.operacionais.domain.service.interfaces;

import com.sistemas.operacionais.domain.model.InteracaoUsuario;

public interface IAdicionaFilaExcedentes {
    // Adiciona um novo cliente na fila de excedentes
    void adicionaExcedente(InteracaoUsuario interacaoUsuario);
}
