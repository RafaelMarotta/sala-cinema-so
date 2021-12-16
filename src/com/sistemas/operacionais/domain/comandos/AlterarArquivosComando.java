package com.sistemas.operacionais.domain.comandos;

import com.sistemas.operacionais.domain.model.ConfiguracoesUsuario;
import com.sistemas.operacionais.exceptions.ComandoInvalidoException;

import java.util.Objects;

public class AlterarArquivosComando {

    private final ConfiguracoesUsuario configuracoesUsuario;

    public AlterarArquivosComando(ConfiguracoesUsuario configuracoesUsuario) {
        this.configuracoesUsuario = configuracoesUsuario;
    }

    public void executar(String[] parametros) {
        if (parametros == null) { //Se os parâmetros forem nulos então lança uma exceção
            throw new ComandoInvalidoException();
        }
        interpretarParametros(parametros); //Chama a função responsável por interpretar os parâmetros
    }

    private void interpretarParametros(String[] parametros) {
        if (parametros.length % 2 != 0) { //Se o número de parametros (Como são KEY, VALUE) não forem par
            throw new ComandoInvalidoException(); // Então lança exceção
        }
        for (int i = 0; i < parametros.length; i++) { //Percorre a lista de parâmetros (OBS: VAI DE 2 EM 2)
            interpretaParametro(parametros[i], parametros[++i]); //E chama o método que interpreta os parâmetros
        }
    }

    private void interpretaParametro(String parametro, String valor) {
        if (Objects.equals(parametro, "-in")) { // Caso parâmetro -in
            configuracoesUsuario.setNomeArquivoInput(valor); //Então seja o nome do arquivo de entrada com o valor posterior ao -in
        } else if (Objects.equals(parametro, "-out")) { // Caso parâmetro -out
            configuracoesUsuario.setNomeArquivoOutput(valor); //Então seja o nome do arquivo de entrada com o valor posterior ao -out
        } else {
            throw new ComandoInvalidoException(); //Caso o comando não exista lança exceção
        }
    }

}
