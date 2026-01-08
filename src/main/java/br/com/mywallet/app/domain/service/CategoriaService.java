package br.com.mywallet.app.domain.service;

import br.com.mywallet.app.domain.model.Categoria.Categoria;
import br.com.mywallet.app.domain.model.Categoria.CategoriaResponseDTO;
import br.com.mywallet.app.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriarepository;

    public List<CategoriaResponseDTO> getAllCategorias(Long usuarioId) {
        List<Categoria> categorias = categoriarepository.findByUsuarioId(usuarioId);

        return categorias.stream()
                .map(cat -> new CategoriaResponseDTO(cat.getId(), cat.getTitulo()))
                .toList();
    }

    public void criarCategoria()
}
