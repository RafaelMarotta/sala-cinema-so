package com.sistemas.operacionais;

public class InteracaoUsuario {

    private int id;
    private String poltrona;
    private String horario;
    private UltimaAcaoEnum ultimaAcao;
    private TipoClienteEnum tipoCliente;
    private boolean tentaBuscaOutraPoltrona;
    private int tempoGasto;

    private static final char DIVISOR_INTERACAO = ';';

    public InteracaoUsuario(String interacao, int line) {
        String[] campos = interacao.split(Character.toString(DIVISOR_INTERACAO));
        id = line;
        poltrona = campos[0];
        horario = campos[1];
        ultimaAcao = UltimaAcaoEnum.obterAcao(campos[2]);
        tentaBuscaOutraPoltrona = campos[3].equals("T");
        tipoCliente = TipoClienteEnum.obterTipoCliente(campos[4]);
        tempoGasto = Integer.parseInt(campos[4]);
    }

    public int getId() {
        return id;
    }

    public String getPoltrona() {
        return poltrona;
    }

    public String getHorario() {
        return horario;
    }

    public UltimaAcaoEnum getUltimaAcao() {
        return ultimaAcao;
    }

    public boolean isTentaBuscaOutraPoltrona() {
        return tentaBuscaOutraPoltrona;
    }

    public int getTempoGasto() {
        return tempoGasto;
    }

    public TipoClienteEnum getTipoCliente() {
        return tipoCliente;
    }
}
