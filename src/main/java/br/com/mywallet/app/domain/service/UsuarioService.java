package br.com.mywallet.app.domain.service;

import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.domain.model.Usuario.UsuarioRequestDTO;
import br.com.mywallet.app.domain.model.exceptions.RegraDeNegocioException;
import br.com.mywallet.app.domain.model.exceptions.ResourceNotFoundException;
import br.com.mywallet.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Usuario> consultarUsuarioPorEmail(String email) {
        Optional<Usuario> usuario =  usuarioRepository.findByEmail(email);

        if (usuario.isEmpty()) { // se o usuario for null
            throw new ResourceNotFoundException("Usuário com e-mail '" + email + "' nâo foi encontrado.");
        }

        return usuario;
    }

    public Optional<Usuario> consultarUsuarioPorId(Long userId) {
        Optional<Usuario> usuario =  usuarioRepository.findById(userId);

        if (usuario.isEmpty()) { // se o usuario for null
            throw new ResourceNotFoundException("Usuário com id '" + userId + "' nâo foi encontrado.");
        }

        return usuario;
    }
}
