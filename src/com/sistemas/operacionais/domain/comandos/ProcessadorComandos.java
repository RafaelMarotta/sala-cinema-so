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
        System.out.print("-> ");
        processarComando(scanner.nextLine());
    }

    private void processarComando(String comando) {
        try {
            String[] strs = comando.split(" ");
            processarComando(strs[0], strs.length > 1 ? Arrays.copyOfRange(strs, 1, strs.length) : null);
            processarComando();
        } catch (Exception ex) {
            System.out.println("Ops, comando inválido. Digite novamente o comando:");
            processarComando();
        }
    }

    private void processarComando(String comando, String[] parametros) {
        switch (comando) {
            case "simular":
                simulaProcessamentoComando.executar(parametros);
                break;
            case "alterar":
                alterarArquivosComando.executar(parametros);
                break;
            case "totalizar":
                totalizarConfirmacoesComando.executar();
                break;
            case "finalizar":
                System.exit(1);
                break;
            default:
                throw new ComandoInvalidoException();
        }
    }

}
