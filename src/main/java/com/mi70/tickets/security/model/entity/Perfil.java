package com.mi70.tickets.security.model.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Perfil implements GrantedAuthority {
    ADMINISTRADOR("Administrador"),
    SENIOR("Senior"),
    PLENO("Pleno"),
    JUNIOR("Junior"),
    USUARIO("Usuario");

    private String descricao;

    Perfil(String descricao) {
        this.descricao = descricao;
    }

    public static Perfil perfilOf(String simpleName) {
        return switch (simpleName) {
            case "Administrador" -> ADMINISTRADOR;
            case "Senior" -> SENIOR;
            case "Pleno" -> PLENO;
            case "Junior" -> JUNIOR;
            case "Usuario" -> USUARIO;
            default -> null;
        };
    }

    @Override
    public String getAuthority() {
        return this.name();
    }

}
