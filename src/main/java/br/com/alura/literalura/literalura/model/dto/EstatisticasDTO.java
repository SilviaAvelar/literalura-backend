package br.com.alura.literalura.literalura.model.dto;

public record EstatisticasDTO(
        long totalLivros,
        double mediaDownloads,
        long maxDownloads,
        long minDownloads,
        long somaDownloads
) {}