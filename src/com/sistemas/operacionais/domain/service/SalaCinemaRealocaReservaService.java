package com.sistemas.operacionais.domain.service;

import com.sistemas.operacionais.domain.model.ConfiguracaoSalaCinema;
import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.Poltrona;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.model.enums.TipoClienteEnum;

import java.util.List;
import java.util.stream.Collectors;

public class SalaCinemaRealocaReservaService {
    protected final SalaCinema salaCinema;
    protected final SalaCinemaQueryService queryService;
    protected final ConfiguracaoSalaCinema configuracaoSalaCinema;
    private final SalaCinemaRealizaReservaService realizaReservaService;

    public SalaCinemaRealocaReservaService(SalaCinema salaCinema, ConfiguracaoSalaCinema configuracaoSalaCinema) {
        this.salaCinema = salaCinema;
        this.queryService = SalaCinemaQueryService.build(salaCinema.obterPoltronas());
        this.configuracaoSalaCinema = configuracaoSalaCinema;
        this.realizaReservaService = new SalaCinemaRealizaReservaService(salaCinema, configuracaoSalaCinema);
    }

    public void realocaReservas() {
        // Trata os casos de excedentes dos clientes clube cinema
        obterClientesExcedentesPorTipoCliente(TipoClienteEnum.CLUBE_CINEMA).forEach(this::realocaReservaClubeCinema);
        // Trata os casos de excedentes dos clientes meia entrada
        obterClientesExcedentesPorTipoCliente(TipoClienteEnum.MEIA_ENTRADA).forEach(this::realocaReservaMeiaEntrada);
        // Trata os casos de excedentes dos clientes regulares
        obterClientesExcedentesPorTipoCliente(TipoClienteEnum.REGULAR).forEach(this::realocaReservaRegular);
    }

    protected void realocaReservaClubeCinema(InteracaoUsuario interacaoUsuario) {
        if (realocaReservaTipoCliente(interacaoUsuario, null)) { // Tenta realocar em uma cadeira vazia
            return; // Conseguiu realocar
        }
        if (realocaReservaTipoCliente(interacaoUsuario, TipoClienteEnum.REGULAR)) {  // Tenta realocar em uma cadeira com um cliente regular
            return; // Conseguiu realocar
        }
        realocaReservaTipoCliente(interacaoUsuario, TipoClienteEnum.MEIA_ENTRADA); // Tenta realocar em uma cadeira com um cliente meia entrada
    }

    protected void realocaReservaMeiaEntrada(InteracaoUsuario interacaoUsuario) {
        if (realocaReservaTipoCliente(interacaoUsuario, null)) {  // Tenta realocar em uma cadeira vazia
            return; // Conseguiu realocar
        }
        realocaReservaTipoCliente(interacaoUsuario, TipoClienteEnum.REGULAR);
    }

    protected void realocaReservaRegular(InteracaoUsuario interacaoUsuario) {
        realocaReservaTipoCliente(interacaoUsuario, null);  // Tenta realocar em uma cadeira com um cliente regular
    }

    // Realoca cadeira de acorda com o tipo de cliente presente na cadeira
    private boolean realocaReservaTipoCliente(InteracaoUsuario interacaoUsuario, TipoClienteEnum tipoCliente) {
        List<Poltrona> poltronasDisponiveis = queryService.obterPoltronasReservadasPorTipoClienteESessao(interacaoUsuario.obterHorario(), tipoCliente);
        if (existeCadeiraDisponivel(poltronasDisponiveis)) {
            return realocaReserva(interacaoUsuario, poltronasDisponiveis.get(0));
        }
        return false;
    }

    // Realiza o processo de realocação de reserva
    private boolean realocaReserva(InteracaoUsuario interacaoUsuario, Poltrona poltrona) {
        queryService.obterPoltrona(interacaoUsuario).obterInteracoes().remove(interacaoUsuario); // Remove a interação da antiga poltrona
        interacaoUsuario.realocaPoltrona(poltrona.obterId()); // Realoca a poltrona do cliente
        realizaReservaService.realizaReserva(poltrona, interacaoUsuario); // Chama o fluxo de realização de reserva
        poltrona.adicionaInteracao(interacaoUsuario); // Adiciona interação na novo poltrona realocada
        salaCinema.obterExcedentes().remove(interacaoUsuario); // Exclui a interação dos excedentes
        return true; // Retorna sucesso
    }

    // Retorna se existem poltronas disponiveis na lista de cadeiras recebida
    private boolean existeCadeiraDisponivel(List<Poltrona> poltronasDisponiveis) {
        return !poltronasDisponiveis.isEmpty();
    }

    // Segrega a lista de interações excedentes por tipo cliente
    public List<InteracaoUsuario> obterClientesExcedentesPorTipoCliente(TipoClienteEnum tipoCliente) {
        return salaCinema.obterExcedentes().stream()
                .filter(e -> e.obterTipoCliente().equals(tipoCliente))
                .collect(Collectors.toList());
    }
}
