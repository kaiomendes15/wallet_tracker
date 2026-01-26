package br.com.mywallet.app.domain.model.Transacao;

import br.com.mywallet.app.domain.enums.TipoTransacao;

import java.time.LocalDate;

public record TransacaoResponseDTO(
        Long transacaoId,
        LocalDate data,
        String descricao,
        Double valor,
        String categoria,
        TipoTransacao tipo
) {}
