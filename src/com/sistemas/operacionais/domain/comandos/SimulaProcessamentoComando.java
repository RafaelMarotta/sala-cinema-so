package com.sistemas.operacionais.domain.comandos;

import com.sistemas.operacionais.domain.model.ConfiguracaoSalaCinema;
import com.sistemas.operacionais.domain.model.ConfiguracoesUsuario;
import com.sistemas.operacionais.domain.model.FilesContextControl;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.service.PopulaLinhasInteracoesService;
import com.sistemas.operacionais.domain.service.SalaCinemaAdicionaInteracoesService;
import com.sistemas.operacionais.exceptions.ComandoInvalidoException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SimulaProcessamentoComando {

    private final SalaCinema salaCinema;
    private final ConfiguracaoSalaCinema configuracaoSalaCinema;
    private final ConfiguracoesUsuario configuracoesUsuario;
    private final Semaphore semaforo;
    private final StringBuilder horariosBuilder;

    public SimulaProcessamentoComando(SalaCinema salaCinema, ConfiguracaoSalaCinema configuracaoSalaCinema, ConfiguracoesUsuario configuracoesUsuario, Semaphore semaforo, StringBuilder horariosBuilder) {
        this.salaCinema = salaCinema;
        this.configuracaoSalaCinema = configuracaoSalaCinema;
        this.configuracoesUsuario = configuracoesUsuario;
        this.semaforo = semaforo;
        this.horariosBuilder = horariosBuilder;
    }

    public void executar(String[] parametros) {
        try {
            if (parametros != null) {
                interpretarParametros(parametros);
            }
            populaInteracoes();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
        if (Objects.equals(parametro, "-log")) {
            if (Objects.equals(valor, "tela")) {
                configuracoesUsuario.setLogTela(true);
            } else if (Objects.equals(valor, "arquivo")) {
                configuracoesUsuario.setLogTela(false);
            } else {
                throw new ComandoInvalidoException();
            }
        }
        if (Objects.equals(parametro, "-pontos")) {
            configuracoesUsuario.setQuantidadeThreads(Integer.parseInt(valor));
        }
    }

    private void populaInteracoes() throws IOException {
        var adicionaInteracoesService = new SalaCinemaAdicionaInteracoesService(salaCinema, configuracaoSalaCinema);
        AtomicInteger idPontoVenda = new AtomicInteger(1);
        List<String> linhas = Files.readAllLines(obterPath(configuracoesUsuario.getNomeArquivoInput()));
        linhas = linhas.stream().filter(l -> !l.startsWith("#")).collect(Collectors.toList());
        FilesContextControl.addLinesToTotalLines(linhas.size());
        int totalPages = Integer.min(linhas.size(), configuracoesUsuario.getQuantidadeThreads());
        int tamanhoPagina = linhas.size()/totalPages;
        for (int i = 0; i < totalPages; i++) {
            List<String> sublist;
            if (i + 1 == totalPages) {
                sublist = linhas.subList(i * tamanhoPagina, linhas.size());
            } else {
                sublist = linhas.subList(i * tamanhoPagina, (i+1)*tamanhoPagina);
            }
            var populaArquivoInteracoesService = new PopulaLinhasInteracoesService(semaforo, adicionaInteracoesService, sublist, idPontoVenda.getAndIncrement(), horariosBuilder);
            populaArquivoInteracoesService.start();
        }
    }

    private Path obterPath(String path) {
        return new File(path).toPath();
    }

}
