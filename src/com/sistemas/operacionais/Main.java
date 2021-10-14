package com.sistemas.operacionais;

import com.sistemas.operacionais.domain.builder.RelatorioInteracoesBuilder;
import com.sistemas.operacionais.domain.builder.SalaCinemaBuilder;
import com.sistemas.operacionais.domain.model.ConfiguracaoSalaCinema;
import com.sistemas.operacionais.domain.model.RelatorioInteracoes;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.service.ObtemConfiguracoesSalaCinemaService;
import com.sistemas.operacionais.domain.service.PopulaArquivoInteracoesService;
import com.sistemas.operacionais.domain.service.SalaCinemaAdicionaInteracoesService;
import com.sistemas.operacionais.domain.service.SalaCinemaRealocaReservaMeiaEntradaService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private static ConfiguracaoSalaCinema configuracaoSalaCinema;
    private static SalaCinema salaCinema;
    private static SalaCinemaRealocaReservaMeiaEntradaService realocaReservaService;

    public static void main(String[] args) throws IOException {
        configuracaoSalaCinema = ObtemConfiguracoesSalaCinemaService.obterConfiguracoesSalaCinema(); // Monta o objeto com as configurações do Cinema
        salaCinema = SalaCinemaBuilder.builder(configuracaoSalaCinema).build(); // Monta o objeto de modelo SalaCinema incluindo as poltronas vazias
        realocaReservaService = new SalaCinemaRealocaReservaMeiaEntradaService(salaCinema, configuracaoSalaCinema); // Instância o service responsável por realocar as reservas
        populaInteracoes(); // Chama o método que irá inciar o procedimento de adição das interações e realização das reservas
        realocaReservasService(); // Chama o método que irá iniciar o procedimento de realocação de clientes em estado excedente
        geraRelatorio(); // Chama o método que irá inicia o procedimento de geração do relatório
    }

    private static void populaInteracoes() throws IOException {
        var adicionaInteracoesService = new SalaCinemaAdicionaInteracoesService(salaCinema, configuracaoSalaCinema);
        var populaArquivoInteracoesService = new PopulaArquivoInteracoesService(adicionaInteracoesService);
        populaArquivoInteracoesService.adicionaInteracoesSalaCinema();
    }

    private static void realocaReservasService() {
        realocaReservaService.realocaReservas();
    }

    private static void geraRelatorio() throws IOException {
        RelatorioInteracoes relatorioInteracoes = RelatorioInteracoesBuilder.builder(salaCinema).build();
        Files.writeString(Path.of("src/com/sistemas/operacionais/resources/vendas.txt"), relatorioInteracoes.toString());
    }

}
