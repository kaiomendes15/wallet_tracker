package br.com.mywallet.app.domain.model.Categoria;

import br.com.mywallet.app.domain.enums.TipoTransacao;

public record CategoriaResponseDTO(Long id, String titulo, TipoTransacao tipo) {}
