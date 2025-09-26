package com.seuorg.sgpe.api.dto;

import com.seuorg.sgpe.domain.Perfil;

public class UsuarioDTO {
  public String nomeCompleto;
  public String cpf;
  public String email;
  public String cargo;
  public String login;
  public String senha;     // texto puro recebido do formulário
  public Perfil perfil;    // ADMINISTRADOR | GERENTE | COLABORADOR
}
