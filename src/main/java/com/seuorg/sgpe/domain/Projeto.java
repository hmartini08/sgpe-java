package com.seuorg.sgpe.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Entity @Table(name="projetos")
public class Projeto {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank @Column(unique = true) private String nome;
  private String descricao;

  @NotNull private LocalDate dataInicio;
  private LocalDate dataTerminoPrevista;

  @Enumerated(EnumType.STRING) @NotNull
  private StatusProjeto status;

  @ManyToOne(optional = false) @JoinColumn(name="gerente_id")
  private Usuario gerente;

  @ManyToMany
  @JoinTable(name="projeto_equipes",
     joinColumns=@JoinColumn(name="projeto_id"),
     inverseJoinColumns=@JoinColumn(name="equipe_id"))
  private Set<Equipe> equipes = new HashSet<>();

  public Long getId(){ return id; }
  public String getNome(){ return nome; }
  public void setNome(String v){ this.nome = v; }
  public String getDescricao(){ return descricao; }
  public void setDescricao(String v){ this.descricao = v; }
  public LocalDate getDataInicio(){ return dataInicio; }
  public void setDataInicio(LocalDate v){ this.dataInicio = v; }
  public LocalDate getDataTerminoPrevista(){ return dataTerminoPrevista; }
  public void setDataTerminoPrevista(LocalDate v){ this.dataTerminoPrevista = v; }
  public StatusProjeto getStatus(){ return status; }
  public void setStatus(StatusProjeto v){ this.status = v; }
  public Usuario getGerente(){ return gerente; }
  public void setGerente(Usuario v){ this.gerente = v; }
  public Set<Equipe> getEquipes(){ return equipes; }
}
