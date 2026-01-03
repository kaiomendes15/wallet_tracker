package br.com.mywallet.app.domain.model.Transacao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Transacao {
    @Id
    private Long id;
}
