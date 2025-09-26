package com.seuorg.sgpe.api.dto;

import com.seuorg.sgpe.domain.StatusProjeto;

public class ProjetoDTO {
    public Long id;
    public String nome;
    public String descricao;
    public String dataInicio; // ISO yyyy-MM-dd (frente já formata)
    public String dataTerminoPrevista; // ISO yyyy-MM-dd
    public StatusProjeto status;

    // dados resumidos do gerente
    public Long gerenteId;
    public String gerenteNome;
    public String gerenteLogin;

    public ProjetoDTO() {
    }

    public ProjetoDTO(
            Long id, String nome, String descricao,
            String dataInicio, String dataTerminoPrevista, StatusProjeto status,
            Long gerenteId, String gerenteNome, String gerenteLogin) {

        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataTerminoPrevista = dataTerminoPrevista;
        this.status = status;
        this.gerenteId = gerenteId;
        this.gerenteNome = gerenteNome;
        this.gerenteLogin = gerenteLogin;
    }
}
