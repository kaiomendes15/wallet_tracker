package br.com.mywallet.app.domain.service;

import br.com.mywallet.app.domain.model.Categoria.Categoria;
import br.com.mywallet.app.domain.model.Categoria.CategoriaDTO;
import br.com.mywallet.app.domain.model.exceptions.ResourceNotFoundException;
import br.com.mywallet.app.repository.CategoriaRepository;
import br.com.mywallet.app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriarepository;

    public List<CategoriaDTO> getAllCategorias(Long usuarioId) {
        List<Categoria> categorias = categoriarepository.findByUsuarioId(usuarioId);

        return categorias.stream()
                .map(cat -> new CategoriaDTO(cat.getId(), cat.getTitulo()))
                .toList();
    }
}
