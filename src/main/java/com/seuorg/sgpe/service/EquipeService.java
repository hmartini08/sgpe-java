package com.seuorg.sgpe.service;

import com.seuorg.sgpe.domain.*;
import com.seuorg.sgpe.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;

@Service
public class EquipeService {
  private final EquipeRepo equipeRepo;
  private final UsuarioRepo usuarioRepo;

  public EquipeService(EquipeRepo e, UsuarioRepo u){ this.equipeRepo = e; this.usuarioRepo = u; }

  @Transactional
  public Equipe criar(String nome, String descricao, java.util.Set<Long> membrosIds){
    Equipe eq = new Equipe();
    eq.setNome(nome);
    eq.setDescricao(descricao);
    if (membrosIds != null && !membrosIds.isEmpty()) {
      var membros = new HashSet<Usuario>(usuarioRepo.findAllById(membrosIds));
      eq.setMembros(membros);
    }
    return equipeRepo.save(eq);
  }
}
