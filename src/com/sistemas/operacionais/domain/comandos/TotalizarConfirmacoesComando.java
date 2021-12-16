package com.sistemas.operacionais.domain.comandos;

import com.sistemas.operacionais.domain.builder.RelatorioInteracoesBuilder;
import com.sistemas.operacionais.domain.model.ConfiguracoesUsuario;
import com.sistemas.operacionais.domain.model.FilesContextControl;
import com.sistemas.operacionais.domain.model.RelatorioInteracoes;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.service.SalaCinemaRealocaReservaService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TotalizarConfirmacoesComando {

    private final SalaCinemaRealocaReservaService realocaReservaService;
    private final SalaCinema salaCinema;
    private final StringBuilder horariosBuilder;
    private final ConfiguracoesUsuario configuracoesUsuario;

    public TotalizarConfirmacoesComando(SalaCinemaRealocaReservaService realocaReservaService, SalaCinema salaCinema, StringBuilder horariosBuilder, ConfiguracoesUsuario configuracoesUsuario) {
        this.realocaReservaService = realocaReservaService;
        this.salaCinema = salaCinema;
        this.horariosBuilder = horariosBuilder;
        this.configuracoesUsuario = configuracoesUsuario;
    }

    public void executar() {
        try {
            realocaReservasEGeraRelatorio();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void realocaReservasEGeraRelatorio() throws IOException, InterruptedException {
        while (!FilesContextControl.isReadComplete()) { // Permanece aguardando enquanto a leitura de todas as linhas ainda não foi concluida
            Thread.sleep(100); // Aguarda 100 milisegundos
            System.out.println("Aguardando ... "); // Exibe na tela a informação: (Aguardando ...)
        }
        realocaReservaService.realocaReservas(); // Chama o método que irá iniciar o procedimento de realocação de clientes em estado excedente
        geraRelatorio(); // Chama o método que irá inicia o procedimento de geração do relatório
    }

    private void geraRelatorio() throws IOException {
        RelatorioInteracoes relatorioInteracoes = RelatorioInteracoesBuilder.builder(salaCinema).build(); //Gera o objeto do relatório
        String relatorio = relatorioInteracoes.toString()+"\n"+"Horários:\n"+horariosBuilder.toString(); //Concatena a parte de horários
        if (configuracoesUsuario.isLogTela()) { //Se o log for na tela
            System.out.println(relatorio); //Então printa no console
        } else {
            Files.writeString(Path.of(configuracoesUsuario.getNomeArquivoOutput()), relatorio); //Se não escreve no arquivo
        }
    }

}
