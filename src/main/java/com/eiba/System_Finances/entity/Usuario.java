package com.eiba.System_Finances.entity;

import com.eiba.System_Finances.Enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password",
        "authorities", "accountNonExpired", "accountNonLocked",
        "credentialsNonExpired", "enabled"})
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Usuario() {}

    public Usuario(String login, String senha, Role role) {
        this.login = login;
        this.senha = senha;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == Role.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_SECRETARIA")
            );
        }
        return List.of(new SimpleGrantedAuthority("ROLE_SECRETARIA"));
    }

    @Override public String getPassword()  { return senha; }
    @Override public String getUsername()  { return login; }
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }

    public UUID getId()    { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getLogin()   { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha()   { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Role getRole()      { return role; }
    public void setRole(Role role) { this.role = role; }
}
