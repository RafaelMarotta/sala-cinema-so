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

    private static ConfiguracaoSalaCinema configuracaoSalaCinema;
    private static SalaCinema salaCinema;
    private static SalaCinemaRealocaReservaMeiaEntradaService realocaReservaService;
    private static ConfiguracoesUsuario configuracoesUsuario;
    private static Semaphore semaforo;
    private static StringBuilder horariosBuilder;
    private static SimulaProcessamentoComando simulaProcessamentoComando;
    private static AlterarArquivosComando alterarArquivosComando;
    private static TotalizarConfirmacoesComando totalizarConfirmacoesComando;
    private static ProcessadorComandos processadorComandos;

    public static void main(String[] args) throws IOException {
        init();
        processadorComandos.processarComando();
    }

    private static void init() throws IOException {
        configuracaoSalaCinema = ObtemConfiguracoesSalaCinemaService.obterConfiguracoesSalaCinema();
        salaCinema = SalaCinemaBuilder.builder(configuracaoSalaCinema).build();
        realocaReservaService = new SalaCinemaRealocaReservaMeiaEntradaService(salaCinema, configuracaoSalaCinema);
        configuracoesUsuario = new ConfiguracoesUsuario();
        semaforo = new Semaphore(1);
        horariosBuilder = new StringBuilder();
        simulaProcessamentoComando = new SimulaProcessamentoComando(salaCinema, configuracaoSalaCinema, configuracoesUsuario, semaforo, horariosBuilder);
        alterarArquivosComando = new AlterarArquivosComando(configuracoesUsuario);
        totalizarConfirmacoesComando = new TotalizarConfirmacoesComando(realocaReservaService, salaCinema, horariosBuilder, configuracoesUsuario);
        processadorComandos = new ProcessadorComandos(simulaProcessamentoComando, alterarArquivosComando, totalizarConfirmacoesComando);
    }

   // public static void main(String[] args) {
        /*semaforo = new Semaphore(1); //Inicia semaforo necessario para controlar a adição de novas interações
        System.out.println("Inicio processamento - " + new Date());
        configuracaoSalaCinema = ObtemConfiguracoesSalaCinemaService.obterConfiguracoesSalaCinema(); // Monta o objeto com as configurações do Cinema
        salaCinema = SalaCinemaBuilder.builder(configuracaoSalaCinema).build(); // Monta o objeto de modelo SalaCinema incluindo as poltronas vazias
        realocaReservaService = new SalaCinemaRealocaReservaMeiaEntradaService(salaCinema, configuracaoSalaCinema); // Instância o service responsável por realocar as reservas
        populaInteracoes(); // Chama o método que irá inciar o procedimento de adição das interações e realização das reservas
        realocaReservasEGeraRelatorio();
        System.out.println("Fim processamento - " + new Date());*/
    //}



}
