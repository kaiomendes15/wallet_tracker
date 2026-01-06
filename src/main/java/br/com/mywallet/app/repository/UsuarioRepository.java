package br.com.mywallet.app.repository;

import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.domain.model.Usuario.UsuarioDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findUserDetailsById(Long id);
    UserDetails findByEmail(String email);

    Optional<UsuarioDTO> findByNomeContaining(String nome);
    boolean existsByEmail(String email);

}
