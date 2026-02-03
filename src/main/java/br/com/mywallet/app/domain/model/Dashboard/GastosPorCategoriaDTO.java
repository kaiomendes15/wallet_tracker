package br.com.mywallet.app.domain.model.Dashboard;

import java.math.BigDecimal;

public record GastosPorCategoriaDTO(
        String categoria,
        BigDecimal valorTotal
) {}
