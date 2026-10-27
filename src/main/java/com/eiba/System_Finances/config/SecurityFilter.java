package com.eiba.System_Finances.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.UsuarioRepository;
import com.eiba.System_Finances.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired TokenService tokenService;
    @Autowired UsuarioRepository usuarioRepository;
    @Autowired AlunoRepository alunoRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = recuperarToken(request);

        if (token != null) {
            DecodedJWT decoded = tokenService.decodificarToken(token);
            if (decoded != null) {
                String tipo = decoded.getClaim("tipo").asString();

                if ("ALUNO".equals(tipo)) {
                    autenticarAluno(decoded);
                } else {
                    autenticarUsuario(decoded.getSubject());
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void autenticarAluno(DecodedJWT decoded) {
        try {
            UUID alunoId = UUID.fromString(decoded.getSubject());
            alunoRepository.findById(alunoId).ifPresent(aluno -> {
                var auth = new UsernamePasswordAuthenticationToken(
                        aluno,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_ALUNO"))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            });
        } catch (IllegalArgumentException ignored) {
            // sub inválido
        }
    }

    private void autenticarUsuario(String login) {
        if (login == null) return;
        UserDetails usuario = usuarioRepository.findByLogin(login).orElse(null);
        if (usuario != null) {
            var auth = new UsernamePasswordAuthenticationToken(
                    usuario, null, usuario.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }

    private String recuperarToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) return null;
        return header.replace("Bearer ", "");
    }
}
