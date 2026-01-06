package br.com.mywallet.app.domain.service;

import br.com.mywallet.app.domain.enums.UserRole;
import br.com.mywallet.app.domain.model.Usuario.AuthenticationRequestDTO;
import br.com.mywallet.app.domain.model.Usuario.AuthenticationResponseDTO;
import br.com.mywallet.app.domain.model.Usuario.RegisterDTO;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.domain.model.exceptions.RegraDeNegocioException;
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

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {
    @Autowired
    UsuarioRepository repository;

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

    public void register(RegisterDTO data) {
        // 1. Validação de Regra de Negócio (Unicidade)
        if (this.repository.findByEmail(data.email()) != null) {
            throw new RegraDeNegocioException("Este e-mail já está cadastrado.");
        }

        // 2. Regra de Negócio (Definição de Role)
        UserRole role = UserRole.USER;
        if (data.email().endsWith("@admin.br")) { // Melhor usar endsWith do que contains para segurança
            role = UserRole.ADMIN;
        }

        // 3. Criptografia
        String encryptedPassword = passwordEncoder.encode(data.senha());

        // 4. Persistência
        Usuario newUser = new Usuario(
                data.nome(),
                data.email(),
                encryptedPassword,
                role
        );

        this.repository.save(newUser);
    }
}
