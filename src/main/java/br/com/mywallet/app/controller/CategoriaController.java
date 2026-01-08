package br.com.mywallet.app.controller;

import br.com.mywallet.app.domain.model.Categoria.CategoriaRequestDTO;
import br.com.mywallet.app.domain.model.Categoria.CategoriaResponseDTO;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.domain.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping("minhas-categorias")
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias(Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        List<CategoriaResponseDTO> categorias = categoriaService.getAllCategorias(usuarioLogado.getId());

        return ResponseEntity.ok(categorias);
    }

    @PostMapping("/criar")
    public ResponseEntity<CategoriaResponseDTO> cadastrarCategoria(@RequestBody @Valid CategoriaRequestDTO data, Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        CategoriaResponseDTO novaCategoria = categoriaService.criarCategoria(data, usuarioLogado);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
    }
}
