package com.seuorg.sgpe.repo;

import com.seuorg.sgpe.domain.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjetoRepo extends JpaRepository<Projeto, Long> {

    @Query("select p from Projeto p join fetch p.gerente")
    List<Projeto> findAllWithGerente();
}
