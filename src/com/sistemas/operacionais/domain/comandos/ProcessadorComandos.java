package com.sistemas.operacionais.domain.comandos;

import com.sistemas.operacionais.exceptions.ComandoInvalidoException;

import java.util.Arrays;
import java.util.Scanner;

public class ProcessadorComandos {

    private final SimulaProcessamentoComando simulaProcessamentoComando;
    private final AlterarArquivosComando alterarArquivosComando;
    private final TotalizarConfirmacoesComando totalizarConfirmacoesComando;
    private final Scanner scanner;

    public ProcessadorComandos(SimulaProcessamentoComando simulaProcessamentoComando, AlterarArquivosComando alterarArquivosComando, TotalizarConfirmacoesComando totalizarConfirmacoesComando) {
        this.simulaProcessamentoComando = simulaProcessamentoComando;
        this.alterarArquivosComando = alterarArquivosComando;
        this.totalizarConfirmacoesComando = totalizarConfirmacoesComando;
        this.scanner = new Scanner(System.in);
    }

    public void processarComando() {
        System.out.print("-> "); //Exibe setinha no console
        processarComando(scanner.nextLine()); //Captura a linha digitada pelo usuário e chama o método para processar o comando
    }

    private void processarComando(String comando) {
        try {
            String[] strs = comando.split(" "); // Quebra os parâmetros pelo espaço em branco
            processarComando(strs[0], strs.length > 1 ? Arrays.copyOfRange(strs, 1, strs.length) : null); //Exclui o comando principal e passa os parametros para frente
        } catch (Exception ex) {
            System.out.println("Ops, comando inválido. Digite novamente o comando:"); //Caso o usuário digite um comando inválido retorna erro
        } finally {
            processarComando(); //Deixa a captura de novos comandos em loop
        }
    }

    private void processarComando(String comando, String[] parametros) {
        switch (comando) {
            case "simular": //Caso o comando seja simular chama a classe responsável por executar esse comando
                simulaProcessamentoComando.executar(parametros);
                break;
            case "alterar": //Caso o comando seja alterar chama a classe responsável pro executar esse comando
                alterarArquivosComando.executar(parametros);
                break;
            case "totalizar": //Caso o comando seja totalizar chama a classe responsável pro executar esse comando
                totalizarConfirmacoesComando.executar();
                break;
            case "finalizar": //Caso o comando seja finalizar então encerra a execução do programa
                System.exit(1);
                break;
            default: //Caso seja um comando inválido lança a exceção "ComandoInvalidoException"
                throw new ComandoInvalidoException();
        }
    }

}
