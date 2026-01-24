package br.com.mywallet.app.domain.service;

import br.com.mywallet.app.domain.exceptions.ResourceNotFoundException;
import br.com.mywallet.app.domain.model.Categoria.Categoria;
import br.com.mywallet.app.domain.model.Transacao.Transacao;
import br.com.mywallet.app.domain.model.Transacao.TransacaoRequestDTO;
import br.com.mywallet.app.domain.model.Transacao.TransacaoResponseDTO;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.repository.CategoriaRepository;
import br.com.mywallet.app.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository repository;

    private final CategoriaRepository categoriaRepository;

    @Transactional // importante para caso haja um método interno que falhe, dando rollback.
    public TransacaoResponseDTO criarTransacao(TransacaoRequestDTO data, Usuario usuarioLogado) {
        Categoria categoria = categoriaRepository.findCategoriaByIdAndUsuarioId(data.categoriaId(), usuarioLogado.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada para o usuário."));

        // Design Pattern: Builder
        Transacao novaTransacao = Transacao.builder()
                .descricao(data.descricao())
                .valor(data.valor())
                .data(data.data())
                .tipo(data.tipo())
                .usuario(usuarioLogado)
                .categoria(categoria)
                .build();


        Transacao transacaoSalva = repository.save(novaTransacao);

        return new TransacaoResponseDTO(
                transacaoSalva.getId(),
                transacaoSalva.getData(),
                transacaoSalva.getDescricao(),
                transacaoSalva.getValor(),
                transacaoSalva.getCategoria().getTitulo(),
                transacaoSalva.getTipo()
        );
    }
}
