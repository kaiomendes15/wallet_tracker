package br.com.mywallet.app.controller;

import br.com.mywallet.app.domain.model.Transacao.TransacaoRequestDTO;
import br.com.mywallet.app.domain.model.Transacao.TransacaoResponseDTO;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.domain.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Page<TransacaoResponseDTO>> listar(
            Authentication authentication,
            // O @PageableDefault define o padrão caso o front não mande nada
            @PageableDefault(page = 0, size = 10, sort = "data", direction = Sort.Direction.DESC) Pageable paginacao
    ) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        Page<TransacaoResponseDTO> pagina = transacaoService.listarTransacoes(usuario, paginacao);

        return ResponseEntity.ok(pagina);
    }
}
