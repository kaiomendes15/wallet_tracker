package br.com.mywallet.app.domain.service;

import br.com.mywallet.app.domain.exceptions.RegraDeNegocioException;
import br.com.mywallet.app.domain.exceptions.ResourceNotFoundException;
import br.com.mywallet.app.domain.model.Categoria.Categoria;
import br.com.mywallet.app.domain.model.Parcelamento.Parcelamento;
import br.com.mywallet.app.domain.model.Transacao.Transacao;
import br.com.mywallet.app.domain.model.Transacao.TransacaoMapper;
import br.com.mywallet.app.domain.model.Transacao.TransacaoRequestDTO;
import br.com.mywallet.app.domain.model.Transacao.TransacaoResponseDTO;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.repository.CategoriaRepository;
import br.com.mywallet.app.repository.ParcelamentoRepository;
import br.com.mywallet.app.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static br.com.mywallet.app.domain.model.Transacao.TransacaoMapper.transacaoParaDto;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository repository;

    private final CategoriaRepository categoriaRepository;

    private final ParcelamentoRepository parcelamentoRepository;

    private TransacaoResponseDTO criarTransacaoUnica(TransacaoRequestDTO data, Usuario usuarioLogado, Categoria categoria) {
        Transacao novaTransacao = Transacao.builder()
                .descricao(data.descricao())
                .valor(data.valor())
                .data(data.data())
                .tipo(data.tipo())
                .usuario(usuarioLogado)
                .categoria(categoria)
                .parcelaAtual(1)
                .totalParcelas(1)
                .parcelamento(null)
                .build();

        Transacao transacaoSalva = repository.save(novaTransacao);
        return transacaoParaDto(transacaoSalva);
    }

    private List<TransacaoResponseDTO> criarParcelamento(TransacaoRequestDTO data, Usuario usuarioLogado, Categoria categoria) {
        // 1. Cria o Header (Parcelamento)
        Parcelamento parcelamento = Parcelamento.builder()
                .descricaoOriginal(data.descricao())
                .valorTotal(data.valor())
                .quantidadeParcelas(data.getTotalParcelas())
                .usuario(usuarioLogado)
                .build();

        // O Hibernate salva o Pai primeiro automaticamente devido ao Cascade,
        // mas instanciar a lista ajuda na lógica abaixo.
        List<Transacao> transacoesParaSalvar = new ArrayList<>();

        // 2. Matemática Financeira (Tratamento de Dízimas/Centavos)
        BigDecimal qtdParcelas = BigDecimal.valueOf(data.getTotalParcelas());

        // Divide arredondando para baixo (Ex: 100 / 3 = 33.33)
        BigDecimal valorParcelaBase = data.valor().divide(qtdParcelas, 2, RoundingMode.DOWN);

        // Calcula quanto "sumiu" no arredondamento (Ex: 100 - 99.99 = 0.01)
        BigDecimal totalCalculado = valorParcelaBase.multiply(qtdParcelas);
        BigDecimal resto = data.valor().subtract(totalCalculado);

        // 3. Loop de Geração
        for (int i = 1; i <= data.getTotalParcelas(); i++) {
            BigDecimal valorDestaParcela = valorParcelaBase;

            // Adiciona os centavos que sobraram na primeira parcela
            if (i == 1) {
                valorDestaParcela = valorDestaParcela.add(resto);
            }

            String descricaoParcela = data.descricao() + " (" + i + "/" + data.getTotalParcelas() + ")";
            Transacao parcela = Transacao.builder()
                    .descricao(descricaoParcela)
                    .valor(valorDestaParcela)
                    .data(data.data().plusMonths(i - 1)) // Data base + meses seguintes
                    .tipo(data.tipo())
                    .usuario(usuarioLogado)
                    .categoria(categoria)
                    .parcelaAtual(i)
                    .totalParcelas(data.getTotalParcelas())
                    .parcelamento(parcelamento) // Vínculo com o Pai
                    .build();

            transacoesParaSalvar.add(parcela);
        }

        // 4. Vincula a lista ao pai (para o Cascade funcionar corretamente se configurado bidirecional)
        parcelamento.setTransacoes(transacoesParaSalvar);

        Parcelamento parcelamentoSalvo = parcelamentoRepository.save(parcelamento);

        // 5. Salva tudo em Batch (Performance)
        // Ao salvar as transações, se o cascade estiver correto, ele salva o parcelamento.
        // Ou salvamos as transações explicitamente.
        List<Transacao> transacoesSalvas = repository.saveAll(transacoesParaSalvar);

        // 6. Retorna a lista de DTOs
        return parcelamentoSalvo.getTransacoes().stream()
                .map(TransacaoMapper::transacaoParaDto)
                .toList();
    }

    @Transactional // importante para caso haja um método interno que falhe, dando rollback.
    public List<TransacaoResponseDTO> criarTransacao(TransacaoRequestDTO data, Usuario usuarioLogado) {
        Categoria categoria = categoriaRepository.findCategoriaByIdAndUsuarioId(data.categoriaId(), usuarioLogado.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada para o usuário."));

        if (categoria.getTipo() != data.tipo()) {
            throw new RegraDeNegocioException(
                    "A categoria " + categoria.getTitulo() + " não aceita lançamentos do tipo " + data.tipo()
            );
        }

        if (data.getTotalParcelas() > 1) {
            return criarParcelamento(data, usuarioLogado, categoria);
        } else {
            // Envolve o retorno único em uma lista imutável
            return List.of(criarTransacaoUnica(data, usuarioLogado, categoria));
        }
    }
    @Transactional(readOnly = true)
    public Page<TransacaoResponseDTO> listarTransacoes(Usuario usuarioLogado, Pageable paginacao) {
        Page<Transacao> paginaTransacoes = repository.findByUsuarioId(usuarioLogado.getId(), paginacao);


        return paginaTransacoes.map(TransacaoMapper::transacaoParaDto);
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
