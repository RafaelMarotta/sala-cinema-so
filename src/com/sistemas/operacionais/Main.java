package com.sistemas.operacionais;

import com.sistemas.operacionais.domain.builder.SalaCinemaBuilder;
import com.sistemas.operacionais.domain.comandos.AlterarArquivosComando;
import com.sistemas.operacionais.domain.comandos.ProcessadorComandos;
import com.sistemas.operacionais.domain.comandos.SimulaProcessamentoComando;
import com.sistemas.operacionais.domain.comandos.TotalizarConfirmacoesComando;
import com.sistemas.operacionais.domain.model.ConfiguracaoSalaCinema;
import com.sistemas.operacionais.domain.model.ConfiguracoesUsuario;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.service.ObtemConfiguracoesSalaCinemaService;
import com.sistemas.operacionais.domain.service.SalaCinemaRealocaReservaMeiaEntradaService;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class Main {

    private static ProcessadorComandos processadorComandos;

    public static void main(String[] args) throws IOException {
        init(); // Inicializa as intâncias dos objetos
        processadorComandos.processarComando(); // Inicializa o interpretador de comandos
    }

    //Cria as intâncias necessarias para rodar o programa
    private static void init() throws IOException {
        ConfiguracaoSalaCinema configuracaoSalaCinema = ObtemConfiguracoesSalaCinemaService.obterConfiguracoesSalaCinema();
        SalaCinema salaCinema = SalaCinemaBuilder.builder(configuracaoSalaCinema).build();
        SalaCinemaRealocaReservaMeiaEntradaService realocaReservaService = new SalaCinemaRealocaReservaMeiaEntradaService(salaCinema, configuracaoSalaCinema);
        ConfiguracoesUsuario configuracoesUsuario = new ConfiguracoesUsuario();
        Semaphore semaforo = new Semaphore(1);
        StringBuilder horariosBuilder = new StringBuilder();
        SimulaProcessamentoComando simulaProcessamentoComando = new SimulaProcessamentoComando(salaCinema, configuracaoSalaCinema, configuracoesUsuario, semaforo, horariosBuilder);
        AlterarArquivosComando alterarArquivosComando = new AlterarArquivosComando(configuracoesUsuario);
        TotalizarConfirmacoesComando totalizarConfirmacoesComando = new TotalizarConfirmacoesComando(realocaReservaService, salaCinema, horariosBuilder, configuracoesUsuario);
        processadorComandos = new ProcessadorComandos(simulaProcessamentoComando, alterarArquivosComando, totalizarConfirmacoesComando);
    }

}
