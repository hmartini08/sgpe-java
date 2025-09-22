package com.seuorg.sgpe.api;

import com.seuorg.sgpe.domain.*;
import com.seuorg.sgpe.repo.*;
import com.seuorg.sgpe.service.ProjetoService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController @RequestMapping("/api/projetos")
public class ProjetoController {
  private final ProjetoService service;
  private final ProjetoRepo repo;
  private final EquipeRepo equipeRepo;

  public ProjetoController(ProjetoService s, ProjetoRepo r, EquipeRepo e){ this.service=s; this.repo=r; this.equipeRepo=e; }

  @PostMapping
  public Projeto criar(@RequestParam String nome,
                       @RequestParam(required=false) String descricao,
                       @RequestParam String dataInicio,
                       @RequestParam(required=false) String dataTerminoPrevista,
                       @RequestParam StatusProjeto status,
                       @RequestParam Long gerenteId){
    LocalDate ini = LocalDate.parse(dataInicio);
    LocalDate fim = (dataTerminoPrevista==null || dataTerminoPrevista.isBlank()) ? null : LocalDate.parse(dataTerminoPrevista);
    return service.criar(nome, descricao, ini, fim, status, gerenteId);
  }

  @PostMapping("/{projetoId}/alocar-equipe/{equipeId}")
  public Projeto alocar(@PathVariable Long projetoId, @PathVariable Long equipeId){
    return service.alocarEquipe(projetoId, equipeId, equipeRepo);
  }

  @GetMapping public List<Projeto> listar(){ return repo.findAll(); }
  @GetMapping("/{id}") public Projeto obter(@PathVariable Long id){ return repo.findById(id).orElseThrow(); }
  @DeleteMapping("/{id}") public void excluir(@PathVariable Long id){ repo.deleteById(id); }
}
