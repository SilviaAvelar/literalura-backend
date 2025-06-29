package br.com.alura.literalura.literalura.service;

import br.com.alura.literalura.literalura.model.Autor;
import br.com.alura.literalura.literalura.model.Livro;
import br.com.alura.literalura.literalura.model.Topico;
import br.com.alura.literalura.literalura.model.dto.*;
import br.com.alura.literalura.literalura.repository.AutorRepository;
import br.com.alura.literalura.literalura.repository.LivroRepository;
import br.com.alura.literalura.literalura.repository.TopicoRepository;
import br.com.alura.literalura.literalura.service.exception.ResourceNotFoundException;
import br.com.alura.literalura.literalura.service.traducao.ConsultaMyMemory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CatalogoService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final TopicoRepository topicoRepository;
    private final ConsumoApi consumoApi;
    private final IConverteDados conversor;
    private final ConsultaMyMemory consultaMyMemory; // <-- INJEÇÃO

    @Value("${literalura.api.baseurl:https://gutendex.com/books/?search=}")
    private String enderecoBaseApi;

    @Autowired
    public CatalogoService(LivroRepository livroRepository,
                           AutorRepository autorRepository,
                           TopicoRepository topicoRepository,
                           ConsumoApi consumoApi,
                           IConverteDados conversor,
                           ConsultaMyMemory consultaMyMemory) { // <-- INJEÇÃO
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.topicoRepository = topicoRepository;
        this.consumoApi = consumoApi;
        this.conversor = conversor;
        this.consultaMyMemory = consultaMyMemory; // <-- INJEÇÃO
    }

    @Transactional
    public Optional<Livro> buscarEsalvarLivroDaApiPorTitulo(String tituloLivro) {
        try {
            String tituloEncoded = URLEncoder.encode(tituloLivro, StandardCharsets.UTF_8.toString());
            var json = consumoApi.obterDados(enderecoBaseApi + tituloEncoded);
            if (json == null || json.isEmpty()) return Optional.empty();

            DadosRespostaApi dadosResposta = conversor.obterDados(json, DadosRespostaApi.class);
            if (dadosResposta == null || dadosResposta.livros() == null || dadosResposta.livros().isEmpty()) {
                return Optional.empty();
            }

            DadosLivro dadosPrimeiroLivro = dadosResposta.livros().get(0);

            Optional<Livro> livroExistente = livroRepository.findByTituloIgnoreCase(dadosPrimeiroLivro.titulo());
            if (livroExistente.isPresent()) {
                System.out.println("INFO: Livro '" + dadosPrimeiroLivro.titulo() + "' já cadastrado.");
                return livroExistente;
            }

            Autor autorEntity = processarAutor(dadosPrimeiroLivro);
            Set<Topico> topicos = processarTopicos(dadosPrimeiroLivro);

            Livro novoLivro = new Livro(dadosPrimeiroLivro);
            novoLivro.setAutor(autorEntity);
            novoLivro.setTopicos(topicos);

            System.out.println("INFO: Salvando novo livro: " + novoLivro.getTitulo());
            return Optional.of(livroRepository.save(novoLivro));

        } catch (Exception e) {
            System.err.println("Erro inesperado no serviço ao buscar e salvar livro: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Autor processarAutor(DadosLivro dadosLivro) {
        if (dadosLivro.autores() == null || dadosLivro.autores().isEmpty()) {
            return null;
        }
        DadosAutor dadosAutorApi = dadosLivro.autores().get(0);
        return autorRepository.findByNomeContainingIgnoreCase(dadosAutorApi.nome())
                .orElseGet(() -> new Autor(dadosAutorApi));
    }

    private Set<Topico> processarTopicos(DadosLivro dadosLivro) {
        Set<Topico> topicos = new HashSet<>();
        if (dadosLivro.subjects() != null) {
            String idiomaDoLivro = dadosLivro.idiomas().isEmpty() ? "en" : dadosLivro.idiomas().get(0);

            for (String nomeTopico : dadosLivro.subjects()) {
                String nomeFinal = nomeTopico;

                if (!idiomaDoLivro.equalsIgnoreCase("en")) {
                    nomeFinal = consultaMyMemory.obterTraducao(nomeTopico, idiomaDoLivro);
                    System.out.println("Traduzido: '" + nomeTopico + "' -> '" + nomeFinal + "'");
                }

                if (nomeFinal.length() > 255) {
                    nomeFinal = nomeFinal.substring(0, 255);
                }

                final String nomeParaLambda = nomeFinal;
                Topico topico = topicoRepository.findByNomeIgnoreCase(nomeParaLambda)
                        .orElseGet(() -> topicoRepository.save(new Topico(nomeParaLambda)));
                topicos.add(topico);
            }
        }
        return topicos;
    }

    @Transactional(readOnly = true)
    public Page<Livro> listarTodosOsLivros(Pageable pageable) {
        return livroRepository.findAllComAutores(pageable);
    }

    @Transactional(readOnly = true)
    public List<Livro> listarTodosOsLivros() {
        return livroRepository.findAllComAutores();
    }

    @Transactional(readOnly = true)
    public Page<Autor> listarTodosOsAutores(Pageable pageable) {
        return autorRepository.findAllComLivros(pageable);
    }

    @Transactional(readOnly = true)
    public List<Autor> listarTodosOsAutores() {
        return autorRepository.findAllComLivros();
    }

    @Transactional(readOnly = true)
    public List<Autor> buscarAutoresPorNome(String nome) {
        List<Autor> autores = autorRepository.findAllByNomeContainingIgnoreCase(nome);
        for (Autor autor : autores) {
            Hibernate.initialize(autor.getLivros());
        }
        return autores;
    }

    @Transactional(readOnly = true)
    public Page<Autor> buscarAutoresPorNome(String nome, Pageable pageable) {
        return autorRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    @Transactional(readOnly = true)
    public List<Livro> buscarTop10LivrosMaisBaixados() {
        return livroRepository.findFirst10ByOrderByNumeroDownloadsDesc();
    }

    @Transactional(readOnly = true)
    public Optional<DoubleSummaryStatistics> calcularEstatisticasDownloads() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) { return Optional.empty(); }
        DoubleSummaryStatistics stats = livros.stream()
                .filter(livro -> livro.getNumeroDownloads() != null && livro.getNumeroDownloads() > 0)
                .mapToDouble(Livro::getNumeroDownloads)
                .summaryStatistics();
        return (stats.getCount() == 0) ? Optional.empty() : Optional.of(stats);
    }

    @Transactional(readOnly = true)
    public List<Autor> listarAutoresPorAnoNascimento(Integer ano) {
        return autorRepository.findByAnoNascimento(ano);
    }

    @Transactional(readOnly = true)
    public Livro buscarLivroPorId(Long id) {
        return livroRepository.findByIdComAutor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Autor> listarAutoresVivosEmDeterminadoAno(int ano, Pageable pageable) {
        return autorRepository.findAutoresVivosNoAno(ano, pageable);
    }

    @Transactional(readOnly = true)
    public List<Autor> listarAutoresVivosEmDeterminadoAno(int ano) {
        return autorRepository.findAutoresVivosNoAno(ano);
    }

    @Transactional(readOnly = true)
    public Page<Livro> listarLivrosPorIdioma(String siglaIdioma, Pageable pageable) {
        return livroRepository.findByIdiomaContainingIgnoreCase(siglaIdioma, pageable);
    }

    @Transactional(readOnly = true)
    public List<Livro> listarTodosLivrosPorIdioma(String siglaIdioma) {
        return livroRepository.findAllByIdiomaContainingIgnoreCase(siglaIdioma);
    }

    @Transactional(readOnly = true)
    public Optional<EstatisticasDTO> obterEstatisticasDeDownloads() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) { return Optional.empty(); }
        DoubleSummaryStatistics stats = livros.stream()
                .filter(livro -> livro.getNumeroDownloads() != null && livro.getNumeroDownloads() > 0)
                .mapToDouble(Livro::getNumeroDownloads)
                .summaryStatistics();

        if (stats.getCount() == 0) { return Optional.empty(); }
        EstatisticasDTO estatisticasDTO = new EstatisticasDTO(
                stats.getCount(), stats.getAverage(), (long) stats.getMax(), (long) stats.getMin(), (long) stats.getSum());
        return Optional.of(estatisticasDTO);
    }
}