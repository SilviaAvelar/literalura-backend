package br.com.alura.literalura.literalura.model.dto;

public record AutorResponseDTO(Long id, String nome, Integer anoNascimento, Integer anoFalecimento) {
    public AutorResponseDTO(br.com.alura.literalura.literalura.model.Autor autor) {
        this(autor.getId(), autor.getNome(), autor.getAnoNascimento(), autor.getAnoFalecimento());
    }
}
