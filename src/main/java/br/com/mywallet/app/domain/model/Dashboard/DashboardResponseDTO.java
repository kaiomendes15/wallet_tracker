package br.com.mywallet.app.domain.model.Dashboard;

import java.math.BigDecimal;

public record DashboardResponseDTO(
        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal saldo
) {
    // Construtor canônico para garantir que não venha nulo do banco
    public DashboardResponseDTO {
        totalReceitas = totalReceitas == null ? BigDecimal.ZERO : totalReceitas;
        totalDespesas = totalDespesas == null ? BigDecimal.ZERO : totalDespesas;
        saldo = saldo == null ? BigDecimal.ZERO : saldo;
    }
}
