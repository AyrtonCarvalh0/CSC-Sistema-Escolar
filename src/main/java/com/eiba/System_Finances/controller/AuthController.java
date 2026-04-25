package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.DTO.CadastroUsuarioDTO;
import com.eiba.System_Finances.DTO.LoginRequestDTO;
import com.eiba.System_Finances.DTO.LoginResponseDTO;
import com.eiba.System_Finances.DTO.TrocarSenhaDTO;
import com.eiba.System_Finances.entity.Usuario;
import com.eiba.System_Finances.repository.UsuarioRepository;
import com.eiba.System_Finances.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:5173"})
public class AuthController {

    @Autowired AuthenticationManager authenticationManager;
    @Autowired UsuarioRepository usuarioRepository;
    @Autowired TokenService tokenService;
    @Autowired PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Login: " + dto.login());
        System.out.println("Senha length: " + (dto.senha() != null ? dto.senha().length() : "NULL"));

        Optional<Usuario> usuarioOpt = usuarioRepository.findByLogin(dto.login());
        if (usuarioOpt.isEmpty()) {
            System.out.println("USUÁRIO NÃO ENCONTRADO NO BANCO");
        } else {
            Usuario u = usuarioOpt.get();
            System.out.println("Usuário encontrado: " + u.getLogin());
            System.out.println("Hash no banco: " + u.getSenha());
            boolean matches = passwordEncoder.matches(dto.senha(), u.getSenha());
            System.out.println("Senha bate: " + matches);
        }

        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
            var auth = authenticationManager.authenticate(usernamePassword);
            var usuario = (Usuario) auth.getPrincipal();
            var token = tokenService.gerarToken(usuario);
            return ResponseEntity.ok(new LoginResponseDTO(token, usuario.getLogin(), usuario.getRole().name()));
        } catch (Exception e) {
            System.out.println("ERRO NA AUTENTICAÇÃO: " + e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }

    @PutMapping("/trocar-senha")
    public ResponseEntity<?> trocarSenha(
            @RequestBody TrocarSenhaDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = usuarioRepository
            .findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())) {
            return ResponseEntity.badRequest().body("Senha atual incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Senha alterada com sucesso!");
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody CadastroUsuarioDTO dto) {
        if (usuarioRepository.findByLogin(dto.login()).isPresent()) {
            return ResponseEntity.badRequest().body("Login já existe");
        }
        String senhaCriptografada = passwordEncoder.encode(dto.senha());
        Usuario novoUsuario = new Usuario(dto.login(), senhaCriptografada, dto.role());
        usuarioRepository.save(novoUsuario);
        return ResponseEntity.status(201).body("Usuário criado com sucesso");
    }
}
