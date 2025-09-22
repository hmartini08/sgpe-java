package com.seuorg.sgpe.repo;

import com.seuorg.sgpe.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepo extends JpaRepository<Usuario, Long> {
  Optional<Usuario> findByLogin(String login);
}
