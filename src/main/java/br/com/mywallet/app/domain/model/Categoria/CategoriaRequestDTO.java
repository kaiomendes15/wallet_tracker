package br.com.mywallet.app.domain.model.Categoria;

import br.com.mywallet.app.domain.enums.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDTO(
        @NotBlank(message = "O título é obrigatório")
        @Size(min = 3, message = "O título deve ter no mínimo 3 caracteres")
        String titulo,

        @NotNull(message = "O tipo da categoria é obrigatório")
        TipoTransacao tipo
) {}
