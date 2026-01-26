package br.com.mywallet.app.repository;

import br.com.mywallet.app.domain.model.Categoria.Categoria;
import br.com.mywallet.app.domain.model.Transacao.Transacao;
import br.com.mywallet.app.domain.enums.TipoTransacao;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    // busca as transações de um usuário
    Page<Transacao> findByUsuarioId(Long usuarioId, Pageable pageable);
    Page<Transacao> findByUsuarioIdOrderByDataDesc(Long usuarioId, Pageable pageable);
    Page<Transacao> findByUsuarioIdOrderByValorDesc(Long usuarioId, Pageable pageable);
    Page<Transacao> findByUsuarioIdOrderByValorAsc(Long usuarioId, Pageable pageable);

    // filtra as transações por tipo (receitas e despesas)
    Page<Transacao> findByUsuarioIdAndTipo(Long usuarioId, TipoTransacao tipo, Pageable pageable);
    // filtra as transações por categoria
    Page<Transacao> findByUsuarioIdAndCategoria(Long usuarioId, Categoria categoria, Pageable pageable);

    // Calcula o total de um tipo (ex: Total de RECEITAS) para um usuário
    // COALESCE(SUM(t.valor), 0): Se a soma for null (sem transações), retorna 0.
    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t WHERE t.usuario.id = :usuarioId AND t.tipo = :tipo")
    Double calcularTotalPorTipo(@Param("usuarioId") Long usuarioId, @Param("tipo") TipoTransacao tipo);

    boolean existsByIdAndUsuarioId(Long id, Long usuarioId);

    void deleteTransacaoById(Long id);

    Optional<Transacao> findTransacaoByIdAndUsuarioId(Long id, Long usuarioId);
}
