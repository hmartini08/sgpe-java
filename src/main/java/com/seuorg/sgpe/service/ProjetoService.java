package com.seuorg.sgpe.service;

import com.seuorg.sgpe.domain.*;
import com.seuorg.sgpe.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjetoService {
  private final ProjetoRepo projetoRepo;
  private final UsuarioRepo usuarioRepo;

  public ProjetoService(ProjetoRepo p, UsuarioRepo u){ this.projetoRepo = p; this.usuarioRepo = u; }

  @Transactional
  public Projeto criar(String nome, String desc, java.time.LocalDate inicio,
                       java.time.LocalDate fimPrev, StatusProjeto status, Long gerenteId){
    Usuario gerente = usuarioRepo.findById(gerenteId)
        .orElseThrow(() -> new IllegalArgumentException("Gerente não encontrado"));
    Projeto pr = new Projeto();
    pr.setNome(nome);
    pr.setDescricao(desc);
    pr.setDataInicio(inicio);
    pr.setDataTerminoPrevista(fimPrev);
    pr.setStatus(status);
    pr.setGerente(gerente);
    return projetoRepo.save(pr);
  }

  @Transactional
  public Projeto alocarEquipe(Long projetoId, Long equipeId, EquipeRepo equipeRepo){
    Projeto p = projetoRepo.findById(projetoId).orElseThrow();
    var eq = equipeRepo.findById(equipeId).orElseThrow();
    p.getEquipes().add(eq);
    return projetoRepo.save(p);
  }
}
