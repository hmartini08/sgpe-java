package com.seuorg.sgpe.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity @Table(name="usuarios")
public class Usuario {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank private String nomeCompleto;
  @NotBlank @Column(unique = true) private String cpf;
  @NotBlank @Email @Column(unique = true) private String email;
  @NotBlank private String cargo;
  @NotBlank @Column(unique = true) private String login;
  @NotBlank private String senhaHash;

  @Enumerated(EnumType.STRING) @NotNull
  private Perfil perfil;

  public Long getId(){ return id; }
  public String getNomeCompleto(){ return nomeCompleto; }
  public void setNomeCompleto(String v){ this.nomeCompleto = v; }
  public String getCpf(){ return cpf; }
  public void setCpf(String v){ this.cpf = v; }
  public String getEmail(){ return email; }
  public void setEmail(String v){ this.email = v; }
  public String getCargo(){ return cargo; }
  public void setCargo(String v){ this.cargo = v; }
  public String getLogin(){ return login; }
  public void setLogin(String v){ this.login = v; }
  public String getSenhaHash(){ return senhaHash; }
  public void setSenhaHash(String v){ this.senhaHash = v; }
  public Perfil getPerfil(){ return perfil; }
  public void setPerfil(Perfil p){ this.perfil = p; }
}
