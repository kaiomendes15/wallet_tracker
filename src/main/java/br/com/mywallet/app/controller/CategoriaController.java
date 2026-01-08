package br.com.mywallet.app.controller;

import br.com.mywallet.app.domain.model.Categoria.CategoriaResponseDTO;
import br.com.mywallet.app.domain.model.Usuario.Usuario;
import br.com.mywallet.app.domain.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping("me")
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias(Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        List<CategoriaResponseDTO> categorias = categoriaService.getAllCategorias(usuarioLogado.getId());

        return ResponseEntity.ok(categorias);
    }
}
