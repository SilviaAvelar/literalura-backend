package br.com.alura.literalura.literalura.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosRespostaApi(
        @JsonAlias("count") Integer total,
        @JsonAlias("results") List<DadosLivro> livros) {
}
