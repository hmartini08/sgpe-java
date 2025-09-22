package com.seuorg.sgpe.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.*;

@Entity @Table(name="equipes")
public class Equipe {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank @Column(unique = true) private String nome;
  private String descricao;

  @ManyToMany
  @JoinTable(name="equipe_membros",
     joinColumns=@JoinColumn(name="equipe_id"),
     inverseJoinColumns=@JoinColumn(name="usuario_id"))
  private Set<Usuario> membros = new HashSet<>();

  public Long getId(){ return id; }
  public String getNome(){ return nome; }
  public void setNome(String v){ this.nome = v; }
  public String getDescricao(){ return descricao; }
  public void setDescricao(String v){ this.descricao = v; }
  public Set<Usuario> getMembros(){ return membros; }
  public void setMembros(Set<Usuario> m){ this.membros = m; }
}
