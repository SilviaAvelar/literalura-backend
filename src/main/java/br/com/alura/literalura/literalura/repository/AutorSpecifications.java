package br.com.alura.literalura.literalura.repository;

import br.com.alura.literalura.literalura.model.Autor;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class AutorSpecifications {

    public static Specification<Autor> nomeContemMultiplasPalavras(String nome) {
        return (root, query, criteriaBuilder) -> {
            if (nome == null || nome.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String[] palavras = nome.trim().split("\\s+");
            List<Predicate> predicados = new ArrayList<>();

            for (String palavra : palavras) {
                String palavraSemAcentos = removerAcentos(palavra.toLowerCase());
                predicados.add(criteriaBuilder.like(
                        criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("nome"))),
                        "%" + palavraSemAcentos + "%"
                ));
            }
            return criteriaBuilder.and(predicados.toArray(new Predicate[0]));
        };
    }

    private static String removerAcentos(String str) {
        if (str == null) return null;
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}