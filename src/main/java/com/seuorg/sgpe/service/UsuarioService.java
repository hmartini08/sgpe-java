package com.seuorg.sgpe.service;

import com.seuorg.sgpe.domain.Usuario;
import com.seuorg.sgpe.domain.Perfil;
import com.seuorg.sgpe.repo.UsuarioRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
  private final UsuarioRepo repo;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public UsuarioService(UsuarioRepo repo){ this.repo = repo; }

  @Transactional
  public Usuario criar(String nome, String cpf, String email, String cargo, String login, String senha, Perfil perfil){
    Usuario u = new Usuario();
    u.setNomeCompleto(nome);
    u.setCpf(cpf);
    u.setEmail(email);
    u.setCargo(cargo);
    u.setLogin(login);
    u.setSenhaHash(encoder.encode(senha));
    u.setPerfil(perfil);
    return repo.save(u);
  }
}
