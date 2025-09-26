package com.seuorg.sgpe.repo;

import com.seuorg.sgpe.domain.Equipe;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EquipeRepo extends JpaRepository<Equipe, Long> {

    // Carrega membros junto para o GET (evita Lazy ao montar DTO)
    @EntityGraph(attributePaths = { "membros" })
    @Query("select e from Equipe e")
    List<Equipe> findAllWithMembros();
}
