package com.seuorg.sgpe.api;

import com.seuorg.sgpe.api.dto.ProjetoDTO;
import com.seuorg.sgpe.domain.Projeto;
import com.seuorg.sgpe.domain.StatusProjeto;
import com.seuorg.sgpe.domain.Usuario;
import com.seuorg.sgpe.repo.ProjetoRepo;
import com.seuorg.sgpe.repo.UsuarioRepo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {

  private final ProjetoRepo repo;
  private final UsuarioRepo usuarioRepo;

  public ProjetoController(ProjetoRepo repo, UsuarioRepo usuarioRepo) {
    this.repo = repo;
    this.usuarioRepo = usuarioRepo;
  }

  // -------- listagem como DTO (evita proxy e lazy)
  @GetMapping
  @Transactional(readOnly = true)
  public List<ProjetoDTO> listar() {
    return repo.findAllWithGerente()
        .stream()
        .map(this::toDTO)
        .toList();
  }

  // -------- criação
  @PostMapping
  @Transactional
  public ProjetoDTO criar(@RequestBody ProjetoDTO dto) {
    Projeto p = new Projeto();
    p.setNome(dto.nome);
    p.setDescricao(dto.descricao);

    if (dto.dataInicio != null && !dto.dataInicio.isBlank()) {
      p.setDataInicio(LocalDate.parse(dto.dataInicio));
    }
    if (dto.dataTerminoPrevista != null && !dto.dataTerminoPrevista.isBlank()) {
      p.setDataTerminoPrevista(LocalDate.parse(dto.dataTerminoPrevista));
    }

    // aceita enum direto ou string
    if (dto.status != null) {
      p.setStatus(dto.status);
    } else {
      p.setStatus(StatusProjeto.PLANEJADO);
    }

    if (dto.gerenteId != null) {
      Usuario gerente = usuarioRepo.findById(dto.gerenteId)
          .orElseThrow(() -> new IllegalArgumentException("Gerente não encontrado: id=" + dto.gerenteId));
      p.setGerente(gerente);
    } else {
      throw new IllegalArgumentException("gerenteId é obrigatório");
    }

    Projeto salvo = repo.save(p);
    // garantir que gerente esteja inicializado para montar DTO
    salvo.getGerente().getId();

    return toDTO(salvo);
  }

  // -------- conversor entidade -> DTO
  private ProjetoDTO toDTO(Projeto p) {
    String di = p.getDataInicio() != null ? p.getDataInicio().toString() : null;
    String dt = p.getDataTerminoPrevista() != null ? p.getDataTerminoPrevista().toString() : null;

    Long gid = null;
    String gnome = null;
    String glogin = null;
    if (p.getGerente() != null) {
      gid = p.getGerente().getId();
      gnome = p.getGerente().getNomeCompleto();
      glogin = p.getGerente().getLogin();
    }

    return new ProjetoDTO(
        p.getId(),
        p.getNome(),
        p.getDescricao(),
        di,
        dt,
        p.getStatus(),
        gid,
        gnome,
        glogin);
  }
}
