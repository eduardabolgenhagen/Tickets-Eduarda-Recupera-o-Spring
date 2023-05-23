package com.mi70.tickets.security.model.entity;

import com.mi70.tickets.model.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJpa implements UserDetails {
    private Usuario usuario;
    private List<Perfil> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private String username;
    private String password;

    public UserJpa(Usuario usuario) {
        this.usuario = usuario;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.password = usuario.getSenha();
        this.username = usuario.getEmail();
        this.authorities = new ArrayList<>();
        this.authorities.add(Perfil.perfilOf(usuario.getClass().getSimpleName()));
    }
}
