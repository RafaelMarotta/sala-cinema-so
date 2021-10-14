package com.sistemas.operacionais.domain.service;

import com.sistemas.operacionais.domain.builder.InteracaoUsuarioBuilder;
import com.sistemas.operacionais.domain.model.ConfiguracaoSalaCinema;
import com.sistemas.operacionais.domain.model.InteracaoUsuario;
import com.sistemas.operacionais.domain.model.Poltrona;
import com.sistemas.operacionais.domain.model.SalaCinema;
import com.sistemas.operacionais.domain.model.enums.UltimaAcaoEnum;

// Receber uma linha do arquivo de interações e adicionar na Poltrona
public class SalaCinemaAdicionaInteracoesService {
    private final SalaCinemaQueryService queryService;
    private final SalaCinemaRealizaReservaService salaCinemaRealizaReservaService;

    public SalaCinemaAdicionaInteracoesService(SalaCinema salaCinema, ConfiguracaoSalaCinema configuracaoSalaCinema) {
        this.queryService = SalaCinemaQueryService.build(salaCinema.obterPoltronas());
        this.salaCinemaRealizaReservaService = new SalaCinemaRealizaReservaService(salaCinema, configuracaoSalaCinema);
    }

    public void adicionaInteracao(String interacao, int line) {
        InteracaoUsuario interacaoUsuario = InteracaoUsuarioBuilder.build(interacao, line); //Cria o objeto de modelo InteracaoUsuario
        Poltrona poltrona = queryService.obterPoltrona(interacaoUsuario); // Obtém o objeto poltrona respectivo da lista de poltronas da sala
        poltrona.adicionaInteracao(interacaoUsuario); // Adiciona na poltrona a interação
        if (UltimaAcaoEnum.ehFluxoReservaCompleto(interacaoUsuario)) { // Se o usuário realizou todo o processo de reserva
            salaCinemaRealizaReservaService.realizaReserva(poltrona, interacaoUsuario); // Chama o service responsável por realizar a reserva
        }
    }
}
