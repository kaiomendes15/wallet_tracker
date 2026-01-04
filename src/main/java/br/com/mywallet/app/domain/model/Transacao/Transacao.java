package br.com.mywallet.app.domain.model.Transacao;

import br.com.mywallet.app.domain.model.Categoria;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.domain.enums.TipoTransacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transacao {
    @Id
    private Long id;
    @NotNull(message = "A descrição é obrigatória") // NotBlank se for String
    private String descricao;
    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser positivo")
    private Double valor;
    @NotNull(message = "A data é obrigatória")
    @PastOrPresent(message = "A data não pode ser futura") // Regra de negócio via Bean Validation
    private LocalDate data;
    @NotNull(message = "O tipo da transação é obrigatório")
    @Enumerated(EnumType.STRING) // Grava "RECEITA" no banco em vez de 0 ou 1
    private TipoTransacao tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

}
