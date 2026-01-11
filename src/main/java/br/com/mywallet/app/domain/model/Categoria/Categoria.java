package br.com.mywallet.app.domain.model.Categoria;

import br.com.mywallet.app.domain.model.Usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false) // Define o nome da coluna no banco (FK)
    private Usuario usuario;

    @Column(nullable = false)
    private String titulo;
}
