package br.com.mywallet.app.controller;

import br.com.mywallet.app.domain.model.Dashboard.DashboardResponseDTO;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.domain.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> receberDashboard(
            Authentication authentication,
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fim
    ) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        DashboardResponseDTO dados = dashboardService.buscarDadosDashboard(usuario, inicio, fim);

        return ResponseEntity.ok(dados);
    }
}
