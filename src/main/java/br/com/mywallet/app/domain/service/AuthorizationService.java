package br.com.mywallet.app.domain.service;

import br.com.mywallet.app.domain.enums.UserRole;
import br.com.mywallet.app.domain.model.Usuario.AuthenticationRequestDTO;
import br.com.mywallet.app.domain.model.Usuario.AuthenticationResponseDTO;
import br.com.mywallet.app.domain.model.Usuario.RegisterDTO;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.domain.exceptions.RegraDeNegocioException;
import br.com.mywallet.app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final CategoriaService categoriaService;

    private final ApplicationContext context; // Truque sênior para pegar o AuthManager sem ciclo
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email);
    }

    public AuthenticationResponseDTO login(AuthenticationRequestDTO data) {
        // Recupera o Bean do AuthenticationManager em tempo de execução
        // Isso evita o erro comum de ciclo: SecurityConfig -> AuthManager -> Service -> SecurityConfig
        AuthenticationManager authenticationManager = context.getBean(AuthenticationManager.class);

        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                data.email(),
                data.senha()
        );

        // O Spring faz a mágica de verificar a senha aqui
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        // Gera o token
        Usuario usuarioAutenticado = (Usuario) auth.getPrincipal();
        String token = tokenService.generateToken(usuarioAutenticado);

        return new AuthenticationResponseDTO(token, usuarioAutenticado.getId());
    }

    @Transactional
    public void register(RegisterDTO data) {
        if (this.repository.findByEmail(data.email()) != null) {
            throw new RegraDeNegocioException("Este e-mail já está cadastrado.");
        }

        // (Definição de Role)
        UserRole role = data.email().endsWith("@admin.br") ? UserRole.ADMIN : UserRole.USER;

        // 3. Criptografia
        String encryptedPassword = passwordEncoder.encode(data.senha());

        // 4. Persistência
        Usuario newUser = Usuario.builder()
                .nome(data.nome())
                .email(data.email())
                .hashSenha(encryptedPassword)
                .role(role)
                .build();

        Usuario novoUsuario = this.repository.save(newUser);

        categoriaService.criarCategoriasPadrao(novoUsuario);
    }
}
