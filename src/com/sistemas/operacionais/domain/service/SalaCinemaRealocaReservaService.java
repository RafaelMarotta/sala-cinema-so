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
        obterClientesExcedentesPorTipoCliente(TipoClienteEnum.CLUBE_CINEMA).forEach(this::realocaReservaClubeCinema);
        obterClientesExcedentesPorTipoCliente(TipoClienteEnum.MEIA_ENTRADA).forEach(this::realocaReservaMeiaEntrada);
        obterClientesExcedentesPorTipoCliente(TipoClienteEnum.REGULAR).forEach(this::realocaReservaRegular);
    }

    protected void realocaReservaClubeCinema(InteracaoUsuario interacaoUsuario) {
        if (realocaReservaTipoCliente(interacaoUsuario, null)) {
            return;
        }
        if (realocaReservaTipoCliente(interacaoUsuario, TipoClienteEnum.REGULAR)) {
            return;
        }
        realocaReservaTipoCliente(interacaoUsuario, TipoClienteEnum.MEIA_ENTRADA);
    }

    protected void realocaReservaMeiaEntrada(InteracaoUsuario interacaoUsuario) {
        if (realocaReservaTipoCliente(interacaoUsuario, null)) {
            return;
        }
        realocaReservaTipoCliente(interacaoUsuario, TipoClienteEnum.REGULAR);
    }

    protected void realocaReservaRegular(InteracaoUsuario interacaoUsuario) {
        realocaReservaTipoCliente(interacaoUsuario, null);
    }

    private boolean realocaReservaTipoCliente(InteracaoUsuario interacaoUsuario, TipoClienteEnum tipoCliente) {
        List<Poltrona> poltronasDisponiveis = queryService.obterPoltronasReservadasPorTipoClienteESessao(interacaoUsuario.obterHorario(), tipoCliente);
        if (existeCadeiraDisponivel(poltronasDisponiveis)) {
            return realocaReserva(interacaoUsuario, poltronasDisponiveis.get(0));
        }
        return false;
    }

    private boolean realocaReserva(InteracaoUsuario interacaoUsuario, Poltrona poltrona) {
        queryService.obterPoltrona(interacaoUsuario).obterInteracoes().remove(interacaoUsuario);
        interacaoUsuario.realocaPoltrona(poltrona.obterId());
        realizaReservaService.realizaReserva(poltrona, interacaoUsuario);
        poltrona.adicionaInteracao(interacaoUsuario);
        salaCinema.obterExcedentes().remove(interacaoUsuario);
        return true;
    }

    private boolean existeCadeiraDisponivel(List<Poltrona> poltronasVazias) {
        return !poltronasVazias.isEmpty();
    }

    public List<InteracaoUsuario> obterClientesExcedentesPorTipoCliente(TipoClienteEnum tipoCliente) {
        return salaCinema.obterExcedentes().stream()
                .filter(e -> e.obterTipoCliente().equals(tipoCliente))
                .collect(Collectors.toList());
    }
}
