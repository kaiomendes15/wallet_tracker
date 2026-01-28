package br.com.mywallet.app.domain.service;

import br.com.mywallet.app.domain.exceptions.RegraDeNegocioException;
import br.com.mywallet.app.domain.model.Dashboard.DashboardResponseDTO;
import br.com.mywallet.app.domain.model.Dashboard.GastosPorCategoriaDTO;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransacaoRepository transacaoRepository;

    @Transactional(readOnly = true)
    public DashboardResponseDTO buscarDadosDashboard(Usuario usuario, LocalDate inicio, LocalDate fim) {

        if (inicio == null || fim == null) {
            LocalDate hoje = LocalDate.now();
            inicio = hoje.withDayOfMonth(1); // Dia 1 do mês atual
            fim = hoje.withDayOfMonth(hoje.lengthOfMonth()); // Último dia do mês
        }

        if (inicio.isAfter(fim)) {
            throw new RegraDeNegocioException("A data de início não pode ser posterior à data fim.");
        }

        return transacaoRepository.buscarDashboard(usuario.getId(), inicio, fim);
    }

    public List<GastosPorCategoriaDTO> buscarGastosPorCategoria(Usuario usuario, LocalDate inicio, LocalDate fim) {

        if (inicio == null || fim == null) {
            LocalDate hoje = LocalDate.now();
            inicio = hoje.withDayOfMonth(1); // Dia 1 do mês atual
            fim = hoje.withDayOfMonth(hoje.lengthOfMonth()); // Último dia do mês
        }

        if (inicio.isAfter(fim)) {
            throw new RegraDeNegocioException("A data de início não pode ser posterior à data fim.");
        }

        List<GastosPorCategoriaDTO> listaCompleta = transacaoRepository.buscarGastosPorCategoria(usuario.getId(), inicio, fim);

        // pega so os 5 primeiros
        return listaCompleta.stream()
                .limit(5)
                .collect(Collectors.toList());
    }


}
