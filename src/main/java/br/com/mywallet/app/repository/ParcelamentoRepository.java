package br.com.mywallet.app.repository;

import br.com.mywallet.app.domain.model.Parcelamento.Parcelamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelamentoRepository extends JpaRepository<Parcelamento, Long> {
}
