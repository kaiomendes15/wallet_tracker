package br.com.mywallet.app.domain.model.Categoria;

public class CategoriaMapper {

    public static CategoriaResponseDTO categoriaParaDto(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getTitulo(),
                categoria.getTipo()
        );
    }
}
