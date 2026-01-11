package br.com.mywallet.app.domain.service;

import br.com.mywallet.app.domain.model.Categoria.Categoria;
import br.com.mywallet.app.domain.model.Categoria.CategoriaRequestDTO;
import br.com.mywallet.app.domain.model.Categoria.CategoriaResponseDTO;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.domain.model.exceptions.RegraDeNegocioException;
import br.com.mywallet.app.domain.model.exceptions.ResourceNotFoundException;
import br.com.mywallet.app.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public CategoriaResponseDTO criarCategoria(CategoriaRequestDTO data, Usuario usuarioLogado) {

        validarNomeDuplicado(data.titulo(), usuarioLogado.getId());

        Categoria novaCategoria = new Categoria();
        novaCategoria.setTitulo(data.titulo());
        novaCategoria.setUsuario(usuarioLogado);

        Categoria categoriaSalva = categoriarepository.save(novaCategoria);

        return new CategoriaResponseDTO(categoriaSalva.getId(), categoriaSalva.getTitulo());
    }

    public CategoriaResponseDTO atualizarCategoria(CategoriaRequestDTO updatedData, Usuario usuarioLogado, Long categoriaId) {

        Categoria categoria = categoriarepository.findCategoriaByIdAndUsuarioId(categoriaId, usuarioLogado.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria com id " + categoriaId + " não foi encontrada para o usuário " + usuarioLogado.getEmail() + "."));

        if (!categoria.getTitulo().equalsIgnoreCase(updatedData.titulo())) {
            validarNomeDuplicado(updatedData.titulo(), usuarioLogado.getId());
        }

        categoria.setTitulo(updatedData.titulo());
        Categoria categoriaAtualizada = categoriarepository.save(categoria);

        return new CategoriaResponseDTO(categoriaAtualizada.getId(), categoriaAtualizada.getTitulo());
    }

    @Transactional
    public void excluirCategoria(Long categoriaId, Usuario usuarioLogado) {
        if (!categoriarepository.existsByIdAndUsuarioId(categoriaId, usuarioLogado.getId())) {
            throw new ResourceNotFoundException("Categoria com id " + categoriaId + " não foi encontrada para o usuário " + usuarioLogado.getEmail() + ".");
        }

        categoriarepository.deleteCategoriaById(categoriaId);
    }

    // Métodos auxiliares
    private void validarNomeDuplicado(String titulo, Long usuarioId) {
        if (categoriarepository.existsByTituloAndUsuarioId(titulo, usuarioId)) {
            throw new RegraDeNegocioException("Você já possui uma categoria com este nome.");
        }
    }
}
