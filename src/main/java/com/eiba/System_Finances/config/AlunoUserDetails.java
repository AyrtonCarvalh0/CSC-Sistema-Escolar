package com.eiba.System_Finances.config;

import com.eiba.System_Finances.entity.Aluno;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AlunoUserDetails implements UserDetails {

    private final Aluno aluno;

    public AlunoUserDetails(Aluno aluno) {
        this.aluno = aluno;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ALUNO"));
    }

    @Override
    public String getUsername() {
        UUID id = aluno.getId();
        String idEmString = id.toString();
        return idEmString;
    }

    @Override
    public String getPassword() { return null; }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}
