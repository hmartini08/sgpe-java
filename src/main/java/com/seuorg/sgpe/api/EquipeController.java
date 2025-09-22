package com.seuorg.sgpe.api;

import com.seuorg.sgpe.domain.Equipe;
import com.seuorg.sgpe.repo.EquipeRepo;
import com.seuorg.sgpe.service.EquipeService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RestController @RequestMapping("/api/equipes")
public class EquipeController {
  private final EquipeService service;
  private final EquipeRepo repo;
  public EquipeController(EquipeService s, EquipeRepo r){ this.service=s; this.repo=r; }

  @PostMapping
  public Equipe criar(@RequestParam String nome,
                      @RequestParam(required=false) String descricao,
                      @RequestParam(required=false) Set<Long> membrosIds){
    return service.criar(nome, descricao, membrosIds);
  }

  @GetMapping public List<Equipe> listar(){ return repo.findAll(); }
  @GetMapping("/{id}") public Equipe obter(@PathVariable Long id){ return repo.findById(id).orElseThrow(); }
  @DeleteMapping("/{id}") public void excluir(@PathVariable Long id){ repo.deleteById(id); }
}
