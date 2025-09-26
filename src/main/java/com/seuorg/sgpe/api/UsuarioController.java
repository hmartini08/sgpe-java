package com.seuorg.sgpe.api;

import com.seuorg.sgpe.api.dto.UsuarioDTO;
import com.seuorg.sgpe.domain.Usuario;
import com.seuorg.sgpe.domain.Perfil;
import com.seuorg.sgpe.repo.UsuarioRepo;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

  private final UsuarioRepo repo;
  private final PasswordEncoder encoder;

  public UsuarioController(UsuarioRepo repo, PasswordEncoder encoder) {
    this.repo = repo;
    this.encoder = encoder;
  }

  // GET /api/usuarios -> lista para popular tabelas/selects
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Usuario> listar() {
    return repo.findAll();
  }

  // POST /api/usuarios (JSON) -> cria usuário
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Usuario> criar(@RequestBody UsuarioDTO dto) {
    // validações básicas (exemplos rápidos)
    if (dto.login == null || dto.login.isBlank() || dto.senha == null || dto.senha.isBlank()) {
      return ResponseEntity.badRequest().build();
    }
    if (dto.perfil == null) {
      dto.perfil = Perfil.COLABORADOR;
    }

    Usuario u = new Usuario();
    u.setNomeCompleto(dto.nomeCompleto);
    u.setCpf(dto.cpf);
    u.setEmail(dto.email);
    u.setCargo(dto.cargo);
    u.setLogin(dto.login);
    // senha cifrada
    u.setSenha(encoder.encode(dto.senha));
    u.setPerfil(dto.perfil);

    Usuario salvo = repo.save(u);
    return ResponseEntity
        .created(URI.create("/api/usuarios/" + salvo.getId()))
        .body(salvo);
  }
}
