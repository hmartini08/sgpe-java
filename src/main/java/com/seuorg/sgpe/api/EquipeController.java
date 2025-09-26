package com.seuorg.sgpe.api;

import com.seuorg.sgpe.api.dto.EquipeDTO;
import com.seuorg.sgpe.domain.Equipe;
import com.seuorg.sgpe.domain.Projeto;
import com.seuorg.sgpe.domain.Usuario;
import com.seuorg.sgpe.repo.EquipeRepo;
import com.seuorg.sgpe.repo.ProjetoRepo;
import com.seuorg.sgpe.repo.UsuarioRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

  private final EquipeRepo equipeRepo;
  private final UsuarioRepo usuarioRepo;
  private final ProjetoRepo projetoRepo;

  public EquipeController(EquipeRepo equipeRepo, UsuarioRepo usuarioRepo, ProjetoRepo projetoRepo) {
    this.equipeRepo = equipeRepo;
    this.usuarioRepo = usuarioRepo;
    this.projetoRepo = projetoRepo;
  }

  // ---------- CREATE ----------
  // Recebe IDs, associa, salva e devolve apenas {id: ...} para evitar serializar
  // coleções LAZY
  @PostMapping
  @Transactional
  public Map<String, Long> criar(@RequestBody EquipeDTO.In dto) {
    Equipe e = new Equipe();
    e.setNome(dto.getNome());
    e.setDescricao(dto.getDescricao());

    if (dto.getMembrosIds() != null && !dto.getMembrosIds().isEmpty()) {
      List<Usuario> usuarios = usuarioRepo.findAllById(dto.getMembrosIds());
      e.setMembros(new HashSet<>(usuarios));
    }

    if (dto.getProjetosIds() != null && !dto.getProjetosIds().isEmpty()) {
      List<Projeto> projetos = projetoRepo.findAllById(dto.getProjetosIds());
      e.setProjetos(new HashSet<>(projetos));
    }

    Equipe salvo = equipeRepo.save(e);
    return Collections.singletonMap("id", salvo.getId());
  }

  // ---------- LIST ----------
  // Lista como DTO, buscando já com membros (EntityGraph) para não estourar Lazy
  // ao mapear
  @GetMapping
  @Transactional(readOnly = true)
  public List<EquipeDTO.Out> listar() {
    List<Equipe> entidades = equipeRepo.findAllWithMembros(); // carrega membros
    return entidades.stream().map(EquipeDTO.Out::fromEntity).collect(Collectors.toList());
  }
}
