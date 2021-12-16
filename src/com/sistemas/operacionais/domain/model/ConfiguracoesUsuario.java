package com.sistemas.operacionais.domain.model;

//Classe modelo responsável por armazenar as configurações setadas pelo usuário
public class ConfiguracoesUsuario {
    private String nomeArquivoInput = "src/com/sistemas/operacionais/resources/interacoes/interacoes_usuarios.txt";
    private String nomeArquivoOutput = "src/com/sistemas/operacionais/resources/vendas.txt";
    private int quantidadeThreads = 1;
    private boolean logTela;

    public String getNomeArquivoInput() {
        return nomeArquivoInput;
    }

    public void setNomeArquivoInput(String nomeArquivoInput) {
        this.nomeArquivoInput = nomeArquivoInput;
    }

    public String getNomeArquivoOutput() {
        return nomeArquivoOutput;
    }

    public void setNomeArquivoOutput(String nomeArquivoOutput) {
        this.nomeArquivoOutput = nomeArquivoOutput;
    }

    public int getQuantidadeThreads() {
        return quantidadeThreads;
    }

    public void setQuantidadeThreads(int quantidadeThreads) {
        this.quantidadeThreads = quantidadeThreads;
    }

    public boolean isLogTela() {
        return logTela;
    }

    public void setLogTela(boolean logTela) {
        this.logTela = logTela;
    }
}
