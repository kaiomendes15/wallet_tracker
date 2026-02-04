package br.com.mywallet.app.controller;

import br.com.mywallet.app.domain.enums.TipoTransacao;
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

import java.util.List;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping("")
    public ResponseEntity<List<TransacaoResponseDTO>> cadastrarTransacao(
            @RequestBody @Valid TransacaoRequestDTO data,
            Authentication authentication
            ) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        List<TransacaoResponseDTO> novaTransacao = transacaoService.criarTransacao(data, usuarioLogado);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaTransacao);
    }

    // Não precisa ter uma rota para cada filtro. Posso deixar a função de filtragem contida na URL da rota. Assim, o
    // frontend é responsável por realizar o filtro
    // exemplo: Receitas de Janeiro ordenadas por valor => {{baseUrl}}/transacoes?page=0&size=10&tipo=RECEITA&mes=1&sort=valor,desc
    @GetMapping
    public ResponseEntity<Page<TransacaoResponseDTO>> listar(
            Authentication authentication,
            @RequestParam(required = false) TipoTransacao tipo, // RECEITA ou DESPESA
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            // O @PageableDefault define o padrão caso o front não mande nada
            @PageableDefault(page = 0, size = 10, sort = "data", direction = Sort.Direction.DESC) Pageable paginacao
    ) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        Page<TransacaoResponseDTO> pagina = transacaoService.listarTransacoes(usuario, paginacao);

        return ResponseEntity.ok(pagina);
    }

    @DeleteMapping("/{transacaoId}")
    public ResponseEntity excluirTransacao(
            @PathVariable Long transacaoId,
            Authentication authentication
    ) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        transacaoService.excluir(transacaoId, usuario);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{transacaoId}")
    public ResponseEntity<TransacaoResponseDTO> atualizar(
            @RequestBody @Valid TransacaoRequestDTO updatedData,
            Authentication authentication,
            @PathVariable Long transacaoId
    ) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        TransacaoResponseDTO transacaoAtualizada = transacaoService.atualizarTransacao(usuario, transacaoId,
                updatedData);

        return ResponseEntity.ok(transacaoAtualizada);
    }
}
