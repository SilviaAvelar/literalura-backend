package br.com.alura.literalura.literalura.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosFormatos(
        @JsonAlias("image/jpeg") String imagemJpeg
) {}
