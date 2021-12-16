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
            if (parametros != null) { //Se parâmetros diferentes de nulo
                interpretarParametros(parametros); //Então processa os parâmetros
            }
            populaInteracoes(); //Popula as interações
        } catch (IOException ex) {
            ex.printStackTrace(); //Exibe o stacktrace caso dê algum erro
        }
    }

    private void interpretarParametros(String[] parametros) {
        if (parametros.length % 2 != 0) { //Se o número de parametros (Como são KEY, VALUE) não forem par
            throw new ComandoInvalidoException(); //Então lança exceção
        }
        for (int i = 0; i < parametros.length; i++) { //Percorre todos os parametros
            interpretaParametro(parametros[i], parametros[++i]); //Chama o método que interpreta os parâmetros
        }
    }

    private void interpretaParametro(String parametro, String valor) {
        if (Objects.equals(parametro, "-log")) { // Caso o nome do parâmetro seja -log
            if (Objects.equals(valor, "tela")) { //Caso o valor do parâmetro seja tela
                configuracoesUsuario.setLogTela(true); //Então seta log em tela para true
            } else if (Objects.equals(valor, "arquivo")) { //Caso o valor do parâmetro seja arquivo
                configuracoesUsuario.setLogTela(false); //Então seta o log em tela para false
            } else {
                throw new ComandoInvalidoException(); //Caso não seja nenhuma das opções a cima então retorna false
            }
        }
        if (Objects.equals(parametro, "-pontos")) { //Caso o nome do parâmetro seja pontos
            configuracoesUsuario.setQuantidadeThreads(Integer.parseInt(valor)); //Então seta a quantidade de threads
        }
    }

    private void populaInteracoes() throws IOException {
        var adicionaInteracoesService = new SalaCinemaAdicionaInteracoesService(salaCinema, configuracaoSalaCinema);
        AtomicInteger idPontoVenda = new AtomicInteger(1);
        List<String> linhas = obterLinhas();
        FilesContextControl.addLinesToTotalLines(linhas.size());
        int totalPages = Integer.min(linhas.size(), configuracoesUsuario.getQuantidadeThreads()); //Obtém o número total de páginas
        int tamanhoPagina = linhas.size()/totalPages; //Obtém o tamanho da página
        for (int i = 0; i < totalPages; i++) { //Percorre todas as páginas
            List<String> sublist;
            if (i + 1 == totalPages) { //Se for última pagina
                sublist = linhas.subList(i * tamanhoPagina, linhas.size()); //Seta final da página como final do arquivo
            } else {
                sublist = linhas.subList(i * tamanhoPagina, (i+1)*tamanhoPagina); //Seta final da página como próxima página * tamanho da página
            }
            //Cria um novo thread e popula as interações
            new PopulaLinhasInteracoesService(semaforo, adicionaInteracoesService, sublist, idPontoVenda.getAndIncrement(), horariosBuilder).start();
        }
    }

    //Obtém todas as linhas filtrando fora os comentários
    private List<String> obterLinhas() throws IOException {
        return Files.readAllLines(obterPath(configuracoesUsuario.getNomeArquivoInput()))
                .stream()
                .filter(l -> !l.startsWith("#")).collect(Collectors.toList());
    }

    //Obtém o Path
    private Path obterPath(String path) {
        return new File(path).toPath();
    }

}
