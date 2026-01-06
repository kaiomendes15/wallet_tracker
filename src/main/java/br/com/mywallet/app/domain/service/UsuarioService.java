package br.com.mywallet.app.domain.service;

import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.domain.model.Usuario.UsuarioDTO;
import br.com.mywallet.app.domain.model.exceptions.ResourceNotFoundException;
import br.com.mywallet.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<UsuarioDTO> consultarUsuarioPorNome(String nome) {
        Optional<UsuarioDTO> usuario =  usuarioRepository.findByNomeContaining(nome);

        if (usuario.isEmpty()) { // se o usuario for null
            throw new ResourceNotFoundException("Usuário com nome '" + nome + "' nâo foi encontrado.");
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
