package com.sistemas.operacionais.domain.comandos;

import com.sistemas.operacionais.domain.model.ConfiguracoesUsuario;
import com.sistemas.operacionais.exceptions.ComandoInvalidoException;

import java.util.Objects;

public class AlterarArquivosComando {

    private ConfiguracoesUsuario configuracoesUsuario;

    public AlterarArquivosComando(ConfiguracoesUsuario configuracoesUsuario) {
        this.configuracoesUsuario = configuracoesUsuario;
    }

    public void executar(String[] parametros) {
        if (parametros == null) {
            throw new ComandoInvalidoException();
        }
        interpretarParametros(parametros);
    }

    private void interpretarParametros(String[] parametros) {
        if (parametros.length % 2 != 0) {
            throw new ComandoInvalidoException();
        }
        for (int i = 0; i < parametros.length; i++) {
            interpretaParametro(parametros[i], parametros[++i]);
        }
    }

    private void interpretaParametro(String parametro, String valor) {
        if (Objects.equals(parametro, "-in")) {
            configuracoesUsuario.setNomeArquivoInput(valor);
        }
        if (Objects.equals(parametro, "-out")) {
            configuracoesUsuario.setNomeArquivoOutput(valor);
        }
    }

}
