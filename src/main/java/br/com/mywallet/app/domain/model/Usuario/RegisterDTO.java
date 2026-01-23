package br.com.mywallet.app.domain.model.Usuario;

import br.com.mywallet.app.domain.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        @NotBlank(message = "A senha é obrigatória")
        String senha,
        Double rendaMensal) {
}
