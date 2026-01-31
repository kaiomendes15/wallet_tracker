package br.com.mywallet.app.domain.service;

import br.com.mywallet.app.domain.enums.TipoTransacao;
import br.com.mywallet.app.domain.model.Categoria.Categoria;
import br.com.mywallet.app.domain.model.Categoria.CategoriaRequestDTO;
import br.com.mywallet.app.domain.model.Categoria.CategoriaResponseDTO;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.domain.exceptions.RegraDeNegocioException;
import br.com.mywallet.app.domain.exceptions.ResourceNotFoundException;
import br.com.mywallet.app.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaResponseDTO> getAllCategorias(Long usuarioId) {
        List<Categoria> categorias = categoriaRepository.findByUsuarioId(usuarioId);

        return categorias.stream()
                .map(cat -> new CategoriaResponseDTO(cat.getId(), cat.getTitulo(), cat.getTipo()))
                .toList();
    }

    public CategoriaResponseDTO criarCategoria(CategoriaRequestDTO data, Usuario usuarioLogado) {

        validarNomeDuplicado(data.titulo(), usuarioLogado.getId());

        Categoria novaCategoria = new Categoria();
        novaCategoria.setTitulo(data.titulo());
        novaCategoria.setUsuario(usuarioLogado);

        Categoria categoriaSalva = categoriaRepository.save(novaCategoria);

        return new CategoriaResponseDTO(categoriaSalva.getId(), categoriaSalva.getTitulo(), categoriaSalva.getTipo());
    }

    public CategoriaResponseDTO atualizarCategoria(CategoriaRequestDTO updatedData, Usuario usuarioLogado, Long categoriaId) {

        Categoria categoria = categoriaRepository.findCategoriaByIdAndUsuarioId(categoriaId, usuarioLogado.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria com id " + categoriaId + " não foi encontrada para o usuário " + usuarioLogado.getEmail() + "."));

        if (!categoria.getTitulo().equalsIgnoreCase(updatedData.titulo())) {
            validarNomeDuplicado(updatedData.titulo(), usuarioLogado.getId());
        }

        categoria.setTitulo(updatedData.titulo());
        Categoria categoriaAtualizada = categoriaRepository.save(categoria);

        return new CategoriaResponseDTO(categoriaAtualizada.getId(), categoriaAtualizada.getTitulo(), categoriaAtualizada.getTipo());
    }

    @Transactional
    public void excluirCategoria(Long categoriaId, Usuario usuarioLogado) {
        if (!categoriaRepository.existsByIdAndUsuarioId(categoriaId, usuarioLogado.getId())) {
            throw new ResourceNotFoundException("Categoria com id " + categoriaId + " não foi encontrada para o usuário " + usuarioLogado.getEmail() + ".");
        }

        categoriaRepository.deleteCategoriaById(categoriaId);
    }

    // Métodos auxiliares
    private void validarNomeDuplicado(String titulo, Long usuarioId) {
        if (categoriaRepository.existsByTituloAndUsuarioId(titulo, usuarioId)) {
            throw new RegraDeNegocioException("Você já possui uma categoria com este nome.");
        }
    }

    public void criarCategoriasPadrao(Usuario usuario) {
        List<Categoria> padroes = new ArrayList<>();

        // Receitas
        padroes.add(criar("Salário", TipoTransacao.RECEITA, usuario));
        padroes.add(criar("Investimentos", TipoTransacao.RECEITA, usuario));

        // Despesas
        padroes.add(criar("Alimentação", TipoTransacao.DESPESA, usuario));
        padroes.add(criar("Lazer", TipoTransacao.DESPESA, usuario));
        padroes.add(criar("Moradia", TipoTransacao.DESPESA, usuario));

        categoriaRepository.saveAll(padroes);
    }

    private Categoria criar(String titulo, TipoTransacao tipo, Usuario usuario) {
        return Categoria.builder()
                .titulo(titulo)
                .tipo(tipo)
                .usuario(usuario)
                .build();
    }
}
