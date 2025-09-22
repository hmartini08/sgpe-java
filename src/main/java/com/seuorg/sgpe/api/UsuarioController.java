package com.seuorg.sgpe.api;

import com.seuorg.sgpe.domain.Perfil;
import com.seuorg.sgpe.domain.Usuario;
import com.seuorg.sgpe.repo.UsuarioRepo;
import com.seuorg.sgpe.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/usuarios")
public class UsuarioController {
  private final UsuarioService service;
  private final UsuarioRepo repo;
  public UsuarioController(UsuarioService s, UsuarioRepo r){ this.service=s; this.repo=r; }

  @PostMapping
  public Usuario criar(@RequestParam String nomeCompleto,
                       @RequestParam String cpf,
                       @RequestParam String email,
                       @RequestParam String cargo,
                       @RequestParam String login,
                       @RequestParam String senha,
                       @RequestParam Perfil perfil){
    return service.criar(nomeCompleto, cpf, email, cargo, login, senha, perfil);
  }

  @GetMapping public List<Usuario> listar(){ return repo.findAll(); }
  @GetMapping("/{id}") public Usuario obter(@PathVariable Long id){ return repo.findById(id).orElseThrow(); }
  @DeleteMapping("/{id}") public void excluir(@PathVariable Long id){ repo.deleteById(id); }
}
