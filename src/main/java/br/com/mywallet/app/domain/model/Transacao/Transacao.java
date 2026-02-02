package br.com.mywallet.app.domain.model.Transacao;

import br.com.mywallet.app.domain.enums.TipoTransacao;
import br.com.mywallet.app.domain.model.Categoria.Categoria;
import br.com.mywallet.app.domain.model.Parcelamento.Parcelamento;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    @NotNull(message = "O valor é obrigatório.")
    @Positive(message = "O valor deve ser positivo.")
    private Double valor;

    @NotNull(message = "A data é obrigatória.")
    // Se mantiver @PastOrPresent, vai dar erro ao criar parcelas para o mês que vem.
    private LocalDate data;

    @NotNull(message = "O tipo da transação é obrigatório.")
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    // Se for compra única, salvar como 1.
    private Integer parcelaAtual;

    // Se for compra única, salvar como 1.
    private Integer totalParcelas;

    // --- RELACIONAMENTOS ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Se for null, é uma transação avulsa.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcelamento_id", nullable = true)
    private Parcelamento parcelamento;

    public void atualizarDados(TransacaoRequestDTO dto, Categoria categoria) {
        setCategoria(categoria);
        setTipo(dto.tipo());
        setData(dto.data());
        setValor(dto.valor());
        setDescricao(dto.descricao());
    }
}