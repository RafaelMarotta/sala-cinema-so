package com.sistemas.operacionais;

import com.sistemas.operacionais.domain.builder.RelatorioInteracoesBuilder;
import com.sistemas.operacionais.domain.builder.SalaCinemaBuilder;
import com.sistemas.operacionais.domain.model.ConfiguracaoSalaCinema;
import com.sistemas.operacionais.domain.model.FilesContextControl;
import com.sistemas.operacionais.domain.model.RelatorioInteracoes;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.service.ObtemConfiguracoesSalaCinemaService;
import com.sistemas.operacionais.domain.service.PopulaArquivoInteracoesService;
import com.sistemas.operacionais.domain.service.SalaCinemaAdicionaInteracoesService;
import com.sistemas.operacionais.domain.service.SalaCinemaRealocaReservaMeiaEntradaService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static ConfiguracaoSalaCinema configuracaoSalaCinema;
    private static SalaCinema salaCinema;
    private static SalaCinemaRealocaReservaMeiaEntradaService realocaReservaService;
    private static Semaphore semaforo;
    private static StringBuilder horariosBuilder = new StringBuilder();

    public static void main(String[] args) throws IOException, InterruptedException {
        semaforo = new Semaphore(1); //Inicia semaforo necessario para controlar a adição de novas interações
        System.out.println("Inicio processamento - " + new Date());
        configuracaoSalaCinema = ObtemConfiguracoesSalaCinemaService.obterConfiguracoesSalaCinema(); // Monta o objeto com as configurações do Cinema
        salaCinema = SalaCinemaBuilder.builder(configuracaoSalaCinema).build(); // Monta o objeto de modelo SalaCinema incluindo as poltronas vazias
        realocaReservaService = new SalaCinemaRealocaReservaMeiaEntradaService(salaCinema, configuracaoSalaCinema); // Instância o service responsável por realocar as reservas
        populaInteracoes(); // Chama o método que irá inciar o procedimento de adição das interações e realização das reservas
        realocaReservasEGeraRelatorio();
        System.out.println("Fim processamento - " + new Date());
    }

    private static void realocaReservasEGeraRelatorio() throws IOException, InterruptedException {
        while (!FilesContextControl.isReadComplete()) { // Permanece aguardando enquanto a leitura de todas as linhas ainda não foi concluida
            Thread.sleep(100); // Aguarda 100 milisegundos
            System.out.println("Aguardando ... "); // Exibe na tela a informação: (Aguardando ...)
        }
        realocaReservasService(); // Chama o método que irá iniciar o procedimento de realocação de clientes em estado excedente
        geraRelatorio(); // Chama o método que irá inicia o procedimento de geração do relatório
    }

    private static void populaInteracoes() throws IOException {
        var adicionaInteracoesService = new SalaCinemaAdicionaInteracoesService(salaCinema, configuracaoSalaCinema);
        AtomicInteger idPontoVenda = new AtomicInteger(1);
        Files.list(obterPathDiretorioInteracoes()).forEach(e -> {
            try {
                var populaArquivoInteracoesService = new PopulaArquivoInteracoesService(semaforo, adicionaInteracoesService, e.toAbsolutePath(), idPontoVenda.getAndIncrement(), horariosBuilder);
                FilesContextControl.addLinesToTotalLines(Files.readAllLines(e).size());
                populaArquivoInteracoesService.start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private static Path obterPathDiretorioInteracoes() {
        return new File("src/com/sistemas/operacionais/resources/interacoes").toPath();
    }

    private static void realocaReservasService() {
        realocaReservaService.realocaReservas();
    }

    private static void geraRelatorio() throws IOException {
        RelatorioInteracoes relatorioInteracoes = RelatorioInteracoesBuilder.builder(salaCinema).build();
        String relatorio = relatorioInteracoes.toString()+"\n"+"Horários:\n"+horariosBuilder.toString();
        Files.writeString(Path.of("src/com/sistemas/operacionais/resources/vendas.txt"), relatorio);
    }

}
