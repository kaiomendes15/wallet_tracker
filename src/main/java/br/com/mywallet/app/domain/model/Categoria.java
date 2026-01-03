package br.com.mywallet.app.domain.model;

import br.com.mywallet.app.domain.model.Usuario.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id") // Define o nome da coluna no banco (FK)
    private Usuario usuario;

    @Column(nullable = false)
    private String titulo;
}
