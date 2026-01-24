package br.com.mywallet.app.domain.model.Transacao;

import br.com.mywallet.app.domain.enums.TipoTransacao;

import java.time.LocalDate;

public record TransacaoResponseDTO(
        Long transacaoId,
        LocalDate data,
        Double valor,
        TipoTransacao tipo
) {}
