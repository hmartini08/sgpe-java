package com.seuorg.sgpe.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projetos") // <- padronizado
public class Projeto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nome;
  @Column(columnDefinition = "text")
  private String descricao;

  private LocalDate dataInicio;
  private LocalDate dataTerminoPrevista;

  @Enumerated(EnumType.STRING)
  private StatusProjeto status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gerente_id", nullable = false)
  private Usuario gerente;

  // lado inverso do ManyToMany (dono está em Equipe)
  @ManyToMany(mappedBy = "projetos")
  private Set<Equipe> equipes = new HashSet<>();

  // getters/setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public LocalDate getDataInicio() {
    return dataInicio;
  }

  public void setDataInicio(LocalDate dataInicio) {
    this.dataInicio = dataInicio;
  }

  public LocalDate getDataTerminoPrevista() {
    return dataTerminoPrevista;
  }

  public void setDataTerminoPrevista(LocalDate dataTerminoPrevista) {
    this.dataTerminoPrevista = dataTerminoPrevista;
  }

  public StatusProjeto getStatus() {
    return status;
  }

  public void setStatus(StatusProjeto status) {
    this.status = status;
  }

  public Usuario getGerente() {
    return gerente;
  }

  public void setGerente(Usuario gerente) {
    this.gerente = gerente;
  }

  public Set<Equipe> getEquipes() {
    return equipes;
  }

  public void setEquipes(Set<Equipe> equipes) {
    this.equipes = equipes;
  }
}
