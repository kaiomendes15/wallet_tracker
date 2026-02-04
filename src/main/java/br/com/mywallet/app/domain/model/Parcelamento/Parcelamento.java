package br.com.mywallet.app.domain.model.Parcelamento;

import br.com.mywallet.app.domain.model.Transacao.Transacao;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parcelamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição original é obrigatória.")
    private String descricaoOriginal;

    @NotNull(message = "O valor total é obrigatório.")
    @Positive
    @Column(precision = 19, scale = 2) // 19 dígitos no total, 2 decimais (ex: 123.45)
    private BigDecimal valorTotal;// Ex: 1200.00

    @NotNull(message = "A quantidade de parcelas é obrigatória.")
    @Positive
    private Integer quantidadeParcelas; // Ex: 12

    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relacionamento Inverso com Cascade:
    // Se deletar este Parcelamento, o banco apaga todas as Transações filhas automaticamente.
    @OneToMany(mappedBy = "parcelamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Transacao> transacoes = new ArrayList<>();
}
