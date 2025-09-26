package com.seuorg.sgpe.domain;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "equipes")
public class Equipe {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nome;

  @Column(columnDefinition = "text")
  private String descricao;

  @ManyToMany
  @JoinTable(
      name = "equipe_membros",
      joinColumns = @JoinColumn(name = "equipe_id"),
      inverseJoinColumns = @JoinColumn(name = "usuario_id")
  )
  private Set<Usuario> membros = new HashSet<>();

  @ManyToMany
  @JoinTable(
      name = "equipe_projeto",
      joinColumns = @JoinColumn(name = "equipe_id"),
      inverseJoinColumns = @JoinColumn(name = "projeto_id")
  )
  private Set<Projeto> projetos = new HashSet<>();

  // getters/setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getNome() { return nome; }
  public void setNome(String nome) { this.nome = nome; }

  public String getDescricao() { return descricao; }
  public void setDescricao(String descricao) { this.descricao = descricao; }

  public Set<Usuario> getMembros() { return membros; }
  public void setMembros(Set<Usuario> membros) { this.membros = membros; }

  public Set<Projeto> getProjetos() { return projetos; }
  public void setProjetos(Set<Projeto> projetos) { this.projetos = projetos; }
}
