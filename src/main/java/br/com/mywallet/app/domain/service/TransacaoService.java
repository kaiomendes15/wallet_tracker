package br.com.mywallet.app.domain.service;

import br.com.mywallet.app.domain.exceptions.ResourceNotFoundException;
import br.com.mywallet.app.domain.model.Categoria.Categoria;
import br.com.mywallet.app.domain.model.Transacao.Transacao;
import br.com.mywallet.app.domain.model.Transacao.TransacaoRequestDTO;
import br.com.mywallet.app.domain.model.Transacao.TransacaoResponseDTO;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.repository.CategoriaRepository;
import br.com.mywallet.app.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.mywallet.app.domain.model.Transacao.TransacaoMapper.transacaoParaDto;

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

    @Transactional(readOnly = true)
    public Page<TransacaoResponseDTO> listarTransacoes(Usuario usuarioLogado, Pageable paginacao) {
        Page<Transacao> paginaTransacoes = repository.findByUsuarioId(usuarioLogado.getId(), paginacao);


        return paginaTransacoes.map(t -> new TransacaoResponseDTO(
                t.getId(),
                t.getData(),
                t.getDescricao(),
                t.getValor(),
                t.getCategoria().getTitulo(),
                t.getTipo()
        ));
    }

    @Transactional
    public void excluir(Long transacaoId, Usuario usuarioLogado) {

        if (!repository.existsByIdAndUsuarioId(transacaoId, usuarioLogado.getId())) {
            throw new ResourceNotFoundException("Transação com id " + transacaoId + " não foi encontrada para o usuário " + usuarioLogado.getEmail() + ".");
        }

        repository.deleteTransacaoById(transacaoId);
    }

    @Transactional
    public TransacaoResponseDTO atualizarTransacao(Usuario usuarioLogado, Long transacaoId,
                                                   TransacaoRequestDTO updatedData) {

        Transacao transacao = repository.findTransacaoByIdAndUsuarioId(transacaoId, usuarioLogado.getId()).orElseThrow(() -> new ResourceNotFoundException("Transação com id " + transacaoId + " não foi encontrada para o usuário " + usuarioLogado.getEmail() + "."));;

        Categoria categoria = categoriaRepository.findCategoriaByIdAndUsuarioId(updatedData.categoriaId(), usuarioLogado.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria com id " + updatedData.categoriaId() + " não foi encontrada para o usuário " + usuarioLogado.getEmail() + "."));

        transacao.atualizarDados(updatedData, categoria);

        // Transacao transacaoAtualizada = repository.save(transacao);
        // GEMINI:
        // Você anotou o metodo com @Transactional. Quando você busca um objeto pelo repositório dentro de uma
        // transação (findTransacaoById...), esse objeto entra em um estado chamado Managed (Gerenciado). O
        // Hibernate/JPA fica "vigiando" esse objeto. Se você altera qualquer atributo dele (setDescricao, setValor, etc.),
        // o Hibernate percebe essa mudança automaticamente. Quando o metodo termina (e o commit da transação acontece),
        // o Hibernate gera o UPDATE no banco sozinho.

        return transacaoParaDto(transacao);
    }
}
