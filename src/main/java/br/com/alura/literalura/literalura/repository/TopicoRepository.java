package br.com.alura.literalura.literalura.repository;

import br.com.alura.literalura.literalura.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    Optional<Topico> findByNomeIgnoreCase(String nome);
}