package br.com.alura.literalura.literalura.controller;

import br.com.alura.literalura.literalura.model.Livro;
import br.com.alura.literalura.literalura.model.dto.AutorResponseDTO;
import br.com.alura.literalura.literalura.model.dto.EstatisticasDTO;
import br.com.alura.literalura.literalura.model.dto.LivroResponseDTO;
import br.com.alura.literalura.literalura.model.dto.PaginatedResponseDTO;

import br.com.alura.literalura.literalura.service.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
public class CatalogoController {

    private final CatalogoService catalogoService;

    @Autowired
    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @PostMapping("/livros/buscar-salvar/titulo/{titulo}")
    public ResponseEntity<LivroResponseDTO> buscarESalvarLivroPelaApi(@PathVariable String titulo) {
        try {
            Optional<Livro> livroOpt = catalogoService.buscarEsalvarLivroDaApiPorTitulo(titulo);

            return livroOpt.map(livro -> ResponseEntity.ok(new LivroResponseDTO(livro)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        } catch (Exception e) {
            System.err.println("Erro no endpoint /livros/buscar-salvar/titulo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/livros")
    public Page<LivroResponseDTO> listarTodosLivros(Pageable pageable) {
        return catalogoService.listarTodosOsLivros(pageable).map(LivroResponseDTO::new);
    }

    @GetMapping("/livros/{id}")
    public ResponseEntity<LivroResponseDTO> obterDetalhesLivro(@PathVariable Long id) {
        Livro livro = catalogoService.buscarLivroPorId(id);
        return ResponseEntity.ok(new LivroResponseDTO(livro));
    }

    @GetMapping("/livros/idioma/{idioma}")
    public Page<LivroResponseDTO> listarLivrosPorIdioma(@PathVariable String idioma, Pageable pageable) {
        return catalogoService.listarLivrosPorIdioma(idioma, pageable)
                .map(LivroResponseDTO::new);
    }

    @GetMapping("/autores")
    public Page<AutorResponseDTO> listarTodosOsAutores(Pageable pageable) {
        return catalogoService.listarTodosOsAutores(pageable)
                .map(AutorResponseDTO::new);
    }

    @GetMapping("/autores/vivos/{ano}")
    public Page<AutorResponseDTO> listarAutoresVivosEmAno(@PathVariable int ano, Pageable pageable) {
        return catalogoService.listarAutoresVivosEmDeterminadoAno(ano, pageable)
                .map(AutorResponseDTO::new);
    }

    @GetMapping("/livros/top10")
    public List<LivroResponseDTO> listarTop10Livros() {
        return catalogoService.buscarTop10LivrosMaisBaixados().stream()
                .map(LivroResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/livros/stats")
    public ResponseEntity<EstatisticasDTO> obterEstatisticas() {
        return catalogoService.obterEstatisticasDeDownloads()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/autores/search")
    public PaginatedResponseDTO<AutorResponseDTO> buscarAutoresPorNome(@RequestParam("nome") String nome, Pageable pageable) {
        Page<AutorResponseDTO> paginaDeDTOs = catalogoService.buscarAutoresPorNome(nome, pageable).map(AutorResponseDTO::new);
        return new PaginatedResponseDTO<>(paginaDeDTOs);
    }
}