package br.com.mywallet.app.domain.model.Dashboard;

import java.math.BigDecimal;

public record DashboardResponseDTO(
        Double totalReceitas,
        Double totalDespesas,
        Double saldo
) {
    // Construtor canônico para garantir que não venha nulo do banco
    public DashboardResponseDTO(Double totalReceitas, Double totalDespesas, Double saldo) {
        this.totalReceitas = totalReceitas != null ? totalReceitas : 0.0;
        this.totalDespesas = totalDespesas != null ? totalDespesas : 0.0;
        this.saldo = saldo != null ? saldo : 0.0;
    }
}
