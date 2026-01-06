package br.com.mywallet.app.domain.model.Usuario;

import br.com.mywallet.app.domain.enums.UserRole;

public record RegisterDTO(String email, String nome, String senha, Double rendaMensal) {
}
