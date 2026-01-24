package br.com.mywallet.app.controller;

import br.com.mywallet.app.domain.model.Transacao.TransacaoRequestDTO;
import br.com.mywallet.app.domain.model.Transacao.TransacaoResponseDTO;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.domain.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping("")
    public ResponseEntity<TransacaoResponseDTO> cadastrarTransacao(
            @RequestBody @Valid TransacaoRequestDTO data,
            Authentication authentication
            ) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        TransacaoResponseDTO novaTransacao = transacaoService.criarTransacao(data, usuarioLogado);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaTransacao);
    }
}
