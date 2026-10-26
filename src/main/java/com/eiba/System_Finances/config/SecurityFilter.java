package com.eiba.System_Finances.config;

import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.UsuarioRepository;
import com.eiba.System_Finances.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
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
            String subject = tokenService.validarToken(token);
            String tipo    = tokenService.extrairTipo(token);

            if (subject != null) {
                UserDetails userDetails;

                if ("ALUNO".equals(tipo)) {
                    UUID alunoId = UUID.fromString(subject);
                    userDetails = alunoRepository.findById(alunoId)
                        .map(AlunoUserDetails::new)
                        .orElse(null);
                } else {
                    userDetails = usuarioRepository.findByLogin(subject).orElse(null);
                }

                if (userDetails != null) {
                    var auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) return null;
        return header.replace("Bearer ", "");
    }
}
