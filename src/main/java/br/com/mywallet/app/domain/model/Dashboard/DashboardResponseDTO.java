package br.com.mywallet.app.domain.model.Dashboard;

import java.math.BigDecimal;

public record DashboardResponseDTO(
        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal saldo
) {
    // Construtor canônico para garantir que não venha nulo do banco
    public DashboardResponseDTO(BigDecimal totalReceitas, BigDecimal totalDespesas, BigDecimal saldo) {
        this.totalReceitas = totalReceitas != null ? totalReceitas : BigDecimal.ZERO;
        this.totalDespesas = totalDespesas != null ? totalDespesas : BigDecimal.ZERO;
        this.saldo = saldo != null ? saldo : BigDecimal.ZERO;
    }
}
