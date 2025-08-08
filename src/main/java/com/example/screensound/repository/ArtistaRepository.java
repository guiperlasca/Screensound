package com.example.screensound.repository;

import com.example.screensound.model.Artista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistaRepository extends JpaRepository <Artista, Long> {
    Optional<Artista> findByNomeContainingIgnoreCase(String nome);
}
