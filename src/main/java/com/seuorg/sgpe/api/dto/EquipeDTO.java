package com.seuorg.sgpe.api.dto;

import com.seuorg.sgpe.domain.Equipe;
import com.seuorg.sgpe.domain.Usuario;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EquipeDTO {

    // Payload de entrada no POST
    public static class In {
        private String nome;
        private String descricao;
        private List<Long> membrosIds;
        private List<Long> projetosIds;

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

        public List<Long> getMembrosIds() {
            return membrosIds;
        }

        public void setMembrosIds(List<Long> membrosIds) {
            this.membrosIds = membrosIds;
        }

        public List<Long> getProjetosIds() {
            return projetosIds;
        }

        public void setProjetosIds(List<Long> projetosIds) {
            this.projetosIds = projetosIds;
        }
    }

    // Payload de saída para GET
    public static class Out {
        private Long id;
        private String nome;
        private String descricao;
        private List<String> membros; // nomes dos membros

        public static Out fromEntity(Equipe e) {
            Out o = new Out();
            o.id = e.getId();
            o.nome = e.getNome();
            o.descricao = e.getDescricao();
            Set<Usuario> set = e.getMembros();
            o.membros = (set == null ? List.of()
                    : set.stream()
                            .map(u -> u.getNomeCompleto() != null ? u.getNomeCompleto() : u.getLogin())
                            .collect(Collectors.toList()));
            return o;
        }

        public Long getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        public String getDescricao() {
            return descricao;
        }

        public List<String> getMembros() {
            return membros;
        }
    }
}
