package br.com.mywallet.app.repository;

import br.com.mywallet.app.domain.model.Categoria.Categoria;
import br.com.mywallet.app.domain.model.Transacao.Transacao;
import br.com.mywallet.app.domain.enums.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    // busca as transações de um usuário
    List<Transacao> findByUsuarioIdOrderByDataDesc(Long usuarioId);
    List<Transacao> findByUsuarioIdOrderByValorDesc(Long usuarioId);
    List<Transacao> findByUsuarioIdOrderByValorAsc(Long usuarioId);

    // filtra as transações por tipo (receitas e despesas)
    List<Transacao> findByUsuarioIdAndTipo(Long usuarioId, TipoTransacao tipo);
    // filtra as transações por categoria
    List<Transacao> findByUsuarioIdAndCategoria(Long usuarioId, Categoria categoria);

    // Calcula o total de um tipo (ex: Total de RECEITAS) para um usuário
    // COALESCE(SUM(t.valor), 0): Se a soma for null (sem transações), retorna 0.
    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t WHERE t.usuario.id = :usuarioId AND t.tipo = :tipo")
    Double calcularTotalPorTipo(@Param("usuarioId") Long usuarioId, @Param("tipo") TipoTransacao tipo);

}
