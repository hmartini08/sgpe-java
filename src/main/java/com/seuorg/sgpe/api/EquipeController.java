package com.seuorg.sgpe.api;

import com.seuorg.sgpe.domain.Perfil;
import com.seuorg.sgpe.domain.Usuario;
import com.seuorg.sgpe.repo.UsuarioRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMINISTRADOR')") // já bate com o SecurityConfig
public class UsuarioController {

  private final UsuarioRepo repo;
  private final BCryptPasswordEncoder encoder;

  public UsuarioController(UsuarioRepo repo, BCryptPasswordEncoder encoder) {
    this.repo = repo;
    this.encoder = encoder;
  }

  @GetMapping
  public List<Usuario> listar() {
    return repo.findAll();
  }

  @PostMapping
  public Usuario criar(@RequestBody Usuario u) {
    if (u.getSenha() != null && !u.getSenha().isBlank()) {
      u.setSenha(encoder.encode(u.getSenha()));
    }
    if (u.getPerfil() == null) {
      u.setPerfil(Perfil.COLABORADOR);
    }
    return repo.save(u);
  }

  // >>> NOVO: atualização por ID (PUT /api/usuarios/{id})
  @PutMapping("/{id}")
  public Usuario atualizar(@PathVariable Long id, @RequestBody Usuario dto) {
    Usuario u = repo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

    u.setNomeCompleto(dto.getNomeCompleto());
    u.setCpf(dto.getCpf());
    u.setEmail(dto.getEmail());
    u.setCargo(dto.getCargo());
    u.setLogin(dto.getLogin());
    if (dto.getPerfil() != null) {
      // Jackson converte "GERENTE"/"ADMINISTRADOR"/"COLABORADOR" para enum Perfil
      u.setPerfil(dto.getPerfil());
    }
    if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
      // senha em branco = manter a atual
      u.setSenha(encoder.encode(dto.getSenha()));
    }

    return repo.save(u);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> remover(@PathVariable Long id) {
    if (!repo.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
    }
    repo.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
