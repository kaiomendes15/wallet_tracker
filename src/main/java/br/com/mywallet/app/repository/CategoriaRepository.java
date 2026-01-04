package br.com.mywallet.app.repository;

import br.com.mywallet.app.domain.model.Categoria;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Lista todas as categorias de UM usuário específico
    List<Categoria> findByUsuarioId(Long usuarioId);
    // Verifica se JÁ EXISTE uma categoria com esse nome PARA um USUÁRIO específico.
    boolean existsByTituloAndUsuario(String titulo, Usuario usuario);
}
