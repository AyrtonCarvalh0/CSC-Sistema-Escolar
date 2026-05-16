package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.DTO.CadastroUsuarioDTO;
import com.eiba.System_Finances.DTO.LoginRequestDTO;
import com.eiba.System_Finances.DTO.LoginResponseDTO;
import com.eiba.System_Finances.DTO.TrocarSenhaDTO;
import com.eiba.System_Finances.entity.Usuario;
import com.eiba.System_Finances.repository.UsuarioRepository;
import com.eiba.System_Finances.service.AuditService;
import com.eiba.System_Finances.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:5173"})
public class AuthController {

    @Autowired AuthenticationManager authenticationManager;
    @Autowired UsuarioRepository usuarioRepository;
    @Autowired TokenService tokenService;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired AuditService auditService;

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return ip != null ? ip.split(",")[0] : request.getRemoteAddr();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto,
                                                   HttpServletRequest request) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
            var auth = authenticationManager.authenticate(usernamePassword);
            var usuario = (Usuario) auth.getPrincipal();
            var token = tokenService.gerarToken(usuario);
            auditService.registrar(
                usuario.getLogin(),
                "LOGIN",
                "USUARIO",
                "Login realizado com sucesso",
                getIp(request)
            );
            return ResponseEntity.ok(new LoginResponseDTO(token, usuario.getLogin(), usuario.getRole().name()));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody CadastroUsuarioDTO dto,
                                        @AuthenticationPrincipal UserDetails userDetails,
                                        HttpServletRequest request) {
        if (usuarioRepository.findByLogin(dto.login()).isPresent()) {
            return ResponseEntity.badRequest().body("Login já existe");
        }
        String senhaCriptografada = passwordEncoder.encode(dto.senha());
        Usuario novoUsuario = new Usuario(dto.login(), senhaCriptografada, dto.role());
        usuarioRepository.save(novoUsuario);
        auditService.registrar(
            userDetails != null ? userDetails.getUsername() : "admin",
            "CRIAR_USUARIO",
            "USUARIO",
            "Usuário criado: " + dto.login() + " | Role: " + dto.role(),
            getIp(request)
        );
        return ResponseEntity.status(201).body("Usuário criado com sucesso");
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Map<String, Object>>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Map<String, Object>> resultado = usuarios.stream()
            .map(u -> {
                Map<String, Object> item = new HashMap<>();
                item.put("id", u.getId());
                item.put("login", u.getLogin());
                item.put("role", u.getRole());
                return item;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable UUID id,
                                             @AuthenticationPrincipal UserDetails userDetails,
                                             HttpServletRequest request) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        auditService.registrar(
            userDetails != null ? userDetails.getUsername() : "admin",
            "DELETAR_USUARIO",
            "USUARIO",
            "Usuário removido: ID " + id,
            getIp(request)
        );
        return ResponseEntity.ok("Usuário removido com sucesso");
    }

    @PutMapping("/trocar-senha")
    public ResponseEntity<?> trocarSenha(
            @RequestBody TrocarSenhaDTO dto,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request) {

        Usuario usuario = usuarioRepository
            .findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())) {
            return ResponseEntity.badRequest().body("Senha atual incorreta");
        }

        if (dto.novaSenha().length() < 6) {
            return ResponseEntity.badRequest().body("A nova senha deve ter pelo menos 6 caracteres");
        }

        usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        usuarioRepository.save(usuario);
        auditService.registrar(
            userDetails.getUsername(),
            "TROCAR_SENHA",
            "USUARIO",
            "Senha alterada",
            getIp(request)
        );
        return ResponseEntity.ok("Senha alterada com sucesso!");
    }

    @PutMapping("/usuarios/{id}/resetar-senha")
    public ResponseEntity<?> resetarSenha(
            @PathVariable UUID id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request) {

        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String novaSenha = body.get("novaSenha");
        if (novaSenha == null || novaSenha.length() < 6) {
            return ResponseEntity.badRequest().body("A senha deve ter pelo menos 6 caracteres");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
        auditService.registrar(
            userDetails != null ? userDetails.getUsername() : "admin",
            "RESETAR_SENHA",
            "USUARIO",
            "Senha resetada para usuário: ID " + id,
            getIp(request)
        );
        return ResponseEntity.ok("Senha resetada com sucesso!");
    }
}
