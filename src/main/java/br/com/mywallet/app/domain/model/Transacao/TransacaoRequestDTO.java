package br.com.mywallet.app.domain.model.Transacao;

import br.com.mywallet.app.domain.enums.TipoTransacao;
import br.com.mywallet.app.domain.model.Categoria.Categoria;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record TransacaoRequestDTO(
        @NotBlank(message = "A descrição é obrigatória.")
        String descricao,

        @NotNull(message = "O valor é obrigatório.")
        @Positive(message = "Valor da transação não pode ser menor que zero.")
        Double valor,

        @NotNull(message = "A data é obrigatória.")
        @PastOrPresent(message = "A data não pode ser futura.")
        LocalDate data,

        @NotNull(message = "O tipo da transação é obrigatório.")
        TipoTransacao tipo,

        @NotNull(message = "A categoria é obrigatória.")
        @Positive(message = "O ID da categoria inválido.")
        Long categoriaId
) {
}
